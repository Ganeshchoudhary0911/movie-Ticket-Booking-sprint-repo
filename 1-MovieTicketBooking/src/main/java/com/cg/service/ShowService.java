package com.cg.service;

import com.cg.dto.ShowDto;
import com.cg.entity.Movie;
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.entity.Theatre;
import com.cg.repository.MovieRepository;
import com.cg.repository.SeatRepository;
import com.cg.repository.ShowRepository;
import com.cg.repository.TheatreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    @Autowired private ShowRepository showRepo;
    @Autowired private TheatreRepository theatreRepo;
    @Autowired private MovieRepository movieRepo;
    @Autowired private SeatRepository seatRepo;

    // ========================= Query methods for your controllers =========================

    @Transactional(readOnly = true)
    public List<ShowDto> getShowsByMovie(Long movieId) {
        List<Show> shows = showRepo.findByMovie_MovieIdOrderByShowDateAscShowTimeAsc(movieId);
        List<ShowDto> out = new ArrayList<>(shows.size());
        for (Show s : shows) {
            out.add(toDto(s));
        }
        return out;
    }

    @Transactional(readOnly = true)
    public List<ShowDto> getAllShows() {
        List<Show> shows = showRepo.findAll();
        List<ShowDto> out = new ArrayList<>(shows.size());
        for (Show s : shows) out.add(toDto(s));
        return out;
    }

    @Transactional(readOnly = true)
    public ShowDto getShowById(Long id) {
        Show s = showRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Show not found: " + id));
        return toDto(s);
    }

    // ========================= Create / Update / Delete =========================

    /**
     * Used by /admin/shows/add (DTO).
     * Creates a Show and auto-generates Seat rows for that show (once).
     */
    @Transactional
    public void addShow(ShowDto dto) {
        Movie movie = movieRepo.findById(dto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid movieId: " + dto.getMovieId()));
        Theatre theatre = theatreRepo.findById(dto.getTheatreId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid theatreId: " + dto.getTheatreId()));

        Show show = new Show();
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(dto.getShowDate());
        show.setShowTime(dto.getShowTime());
        show.setPrice(dto.getPrice());

        createShowWithSeats(show); // handles idempotent seat creation
    }

    /**
     * Used by /admin/shows/update/{id}.
     * If theatre changes, seats are regenerated.
     * If price changes, seat prices are updated.
     */
    @Transactional
    public void updateShow(Long id, ShowDto dto) {
        Show s = showRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Show not found: " + id));

        boolean theatreChanged = (dto.getTheatreId() != null)
                && !dto.getTheatreId().equals(s.getTheatre().getTheatreId());
        boolean priceChanged = dto.getPrice() != s.getPrice();

        if (dto.getMovieId() != null && !dto.getMovieId().equals(s.getMovie().getMovieId())) {
            Movie m = movieRepo.findById(dto.getMovieId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid movieId: " + dto.getMovieId()));
            s.setMovie(m);
        }
        if (dto.getTheatreId() != null && theatreChanged) {
            Theatre t = theatreRepo.findById(dto.getTheatreId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid theatreId: " + dto.getTheatreId()));
            s.setTheatre(t);
        }
        if (dto.getShowDate() != null) s.setShowDate(dto.getShowDate());
        if (dto.getShowTime() != null) s.setShowTime(dto.getShowTime());
        s.setPrice(dto.getPrice());

        showRepo.save(s);

        // If theatre changed ⇒ delete old seats and regenerate for this show
        if (theatreChanged) {
            seatRepo.deleteByShowId(s.getShowId());
            generateSeatsForShow(s, /*seatsPerRow*/ 10);
        } else if (priceChanged) {
            // Optional: keep Seat.seatPrice in sync with Show.price
            seatRepo.updateSeatPricesForShow(s.getShowId(), s.getPrice());
        }
    }

    @Transactional
    public void deleteShow(Long id) {
        // Seats reference Show (ManyToOne), so we must delete Seats first
        seatRepo.deleteByShowId(id);
        showRepo.deleteById(id);
    }

    // ========================= Core: Create show + auto-generate seats =========================

    /**
     * Used by your /shows POST endpoint, and internally by addShow().
     * Idempotent: will not duplicate seats if already generated.
     */
    @Transactional
    public Show createShowWithSeats(Show showRequest) {
        // Resolve managed Movie & Theatre if only IDs are set on detached instances
        if (showRequest.getMovie() != null && showRequest.getMovie().getMovieId() != null) {
            Movie m = movieRepo.findById(showRequest.getMovie().getMovieId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid movieId: " + showRequest.getMovie().getMovieId()));
            showRequest.setMovie(m);
        }
        if (showRequest.getTheatre() != null && showRequest.getTheatre().getTheatreId() != null) {
            Theatre t = theatreRepo.findById(showRequest.getTheatre().getTheatreId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid theatreId: " + showRequest.getTheatre().getTheatreId()));
            showRequest.setTheatre(t);
        }

        Show saved = showRepo.save(showRequest);

        if (!seatRepo.existsByShow_ShowId(saved.getShowId())) {
            generateSeatsForShow(saved, /*seatsPerRow*/ 10);
        }
        return saved;
    }

    // ========================= Helpers =========================

    private void generateSeatsForShow(Show show, int seatsPerRow) {
        int totalSeats = show.getTheatre().getTotalSeats();
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        List<Seat> toSave = new ArrayList<>(totalSeats);
        int count = 0;

        for (int r = 0; r < rows; r++) {
            String rowLabel = rowIndexToLabel(r); // A, B, ..., Z, AA, AB...
            for (int c = 1; c <= seatsPerRow && count < totalSeats; c++) {
                Seat s = new Seat();
                s.setSeatRow(rowLabel);
                s.setSeatNumber(String.valueOf(c));
                s.setSeatType("REGULAR");
                s.setSeatPrice(show.getPrice());
                s.setBooked(false);
                s.setShow(show);
                toSave.add(s);
                count++;
            }
        }
        seatRepo.saveAll(toSave);
    }

    private String rowIndexToLabel(int idx) {
        StringBuilder sb = new StringBuilder();
        idx += 1;
        while (idx > 0) {
            int rem = (idx - 1) % 26;
            sb.append((char) ('A' + rem));
            idx = (idx - 1) / 26;
        }
        return sb.reverse().toString();
    }

    private ShowDto toDto(Show s) {
        ShowDto dto = new ShowDto();
        dto.setShowId(s.getShowId());
        dto.setShowDate(s.getShowDate());
        dto.setShowTime(s.getShowTime());
        dto.setPrice(s.getPrice());
        dto.setMovieId(s.getMovie() != null ? s.getMovie().getMovieId() : null);
        dto.setTheatreId(s.getTheatre() != null ? s.getTheatre().getTheatreId() : null);
        dto.setMovieName(s.getMovie() != null ? s.getMovie().getMovieName() : null);
        dto.setTheatreName(s.getTheatre() != null ? s.getTheatre().getTheatreName() : null);
        return dto;
    }
}