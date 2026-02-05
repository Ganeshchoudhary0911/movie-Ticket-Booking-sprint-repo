package com.cg.service;
 
import java.time.LocalDate;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.cg.entity.Show;
import com.cg.repository.ShowRepository;
 
@Service
public class ShowService {
 
    @Autowired
    private ShowRepository showRepository;
 
    // ================= CREATE =================
    public Show addShow(Show show) {
        return showRepository.save(show);
    }
 
    // ================= UPDATE =================
    public Show updateShow(Long id, Show updated) {
 
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));
 
        show.setShowDate(updated.getShowDate());
        show.setShowTime(updated.getShowTime());
        show.setPrice(updated.getPrice());
        show.setMovie(updated.getMovie());
        show.setTheatre(updated.getTheatre());
 
        return showRepository.save(show);
    }
 
    // ================= DELETE =================
    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }
 
    // ================= GET BY ID =================
    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));
    }
 
    // ================= USER SIDE (IMPORTANT) =================
    // Fetch shows ONLY for selected movie
    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieMovieId(movieId);
    }
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }
    
//    public List<Show> getShowsByMovieAndDate(long movieId, LocalDate date) {
//        return showRepository.findByMovieMovieIdAndShowDate(movieId, date);
//    }
    public List<Show> getShowsByMovieAndDate(Long movieId, LocalDate date) {
        return showRepository.findByMovieMovieIdAndShowDate(movieId, date);
    }
     
}
 