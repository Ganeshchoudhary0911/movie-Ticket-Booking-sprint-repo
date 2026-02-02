package com.cg.service;

import com.cg.entity.Theatre;
import java.util.List;

public interface ITheatreService {

    Theatre addTheatre(Theatre theatre);

    Theatre updateTheatre(Long theatreId, Theatre theatre);

    void deleteTheatre(Long theatreId);

    Theatre getTheatreById(Long theatreId);

    List<Theatre> getAllTheatres();

    List<Theatre> getTheatresByCity(String city);
}