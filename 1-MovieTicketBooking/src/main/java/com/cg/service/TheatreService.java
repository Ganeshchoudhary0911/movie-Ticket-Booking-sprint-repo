package com.cg.service;

import com.cg.entity.Theatre;
import com.cg.repository.TheatreRepository;
import com.cg.service.TheatreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService implements ITheatreService {

	@Autowired
    TheatreRepository theatreRepository;

    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @Override
    public Theatre addTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    @Override
    public Theatre updateTheatre(Long theatreId, Theatre updated) {
        Theatre t = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        t.setTheatreName(updated.getTheatreName());
        t.setLocation(updated.getLocation());
        t.setCity(updated.getCity());
        t.setTotalSeats(updated.getTotalSeats());
        t.setContactNumber(updated.getContactNumber());

        return theatreRepository.save(t);
    }

    @Override
    public void deleteTheatre(Long theatreId) {
        theatreRepository.deleteById(theatreId);
    }

    @Override
    public Theatre getTheatreById(Long theatreId) {
        return theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @Override
    public List<Theatre> getTheatresByCity(String city) {
        return theatreRepository.findByCityIgnoreCase(city);
    }
}