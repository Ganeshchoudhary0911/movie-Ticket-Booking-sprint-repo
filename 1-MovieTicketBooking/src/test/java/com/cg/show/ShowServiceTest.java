package com.cg.show;

import com.cg.dto.ShowDto;
import com.cg.entity.Movie;
import com.cg.entity.Show;
import com.cg.entity.Theatre;
import com.cg.repository.MovieRepository;
import com.cg.repository.ShowRepository;
import com.cg.repository.TheatreRepository;
import com.cg.service.ShowService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @Mock private ShowRepository showRepository;
    @Mock private MovieRepository movieRepository;
    @Mock private TheatreRepository theatreRepository;

    @InjectMocks
    private ShowService showService;

    private Movie movie;
    private Theatre theatre;
    private Show show;

    @BeforeEach
    void setup() {
        movie = new Movie();
        movie.setMovieId(1L);
        movie.setMovieName("KGF");

        theatre = new Theatre();
        theatre.setTheatreId(1L);
        theatre.setTheatreName("PVR");

        show = new Show();
        show.setShowId(1L);
        show.setShowDate(LocalDate.now());
        show.setShowTime(LocalTime.NOON);
        show.setPrice(200);
        show.setMovie(movie);
        show.setTheatre(theatre);
    }

    @Test
    void testAddShow() {

        ShowDto dto = new ShowDto();
        dto.setMovieId(1L);
        dto.setTheatreId(1L);
        dto.setShowDate(LocalDate.now());
        dto.setShowTime(LocalTime.NOON);
        dto.setPrice(200);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        when(showRepository.save(any(Show.class))).thenReturn(show);

        ShowDto saved = showService.addShow(dto);

        assertEquals(200, saved.getPrice());
        assertEquals("KGF", saved.getMovieName());
        verify(showRepository).save(any());
    }

    @Test
    void testGetAllShows() {
        when(showRepository.findAll()).thenReturn(List.of(show));

        List<ShowDto> list = showService.getAllShows();

        assertEquals(1, list.size());
    }

    @Test
    void testGetShowById() {
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));

        ShowDto dto = showService.getShowById(1L);

        assertEquals(1L, dto.getShowId());
    }

    @Test
    void testDeleteShow() {
        showService.deleteShow(1L);
        verify(showRepository).deleteById(1L);
    }
}
