package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Show;
import com.cg.repository.ShowRepository;

@Service
public class ShowService implements IShowService {
 
    @Autowired
    private ShowRepository showRepository;
 
    @Override
    public Show addShow(Show show) {
        return showRepository.save(show);
    }
 
    @Override
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
 
    @Override
    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }
 
    @Override
    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));
    }
 
    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }
}
 
	
	


