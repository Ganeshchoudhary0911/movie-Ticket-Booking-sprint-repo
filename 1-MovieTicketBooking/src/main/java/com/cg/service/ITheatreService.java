package com.cg.service;

import com.cg.dto.TheatreDto;
import java.util.List;

public interface ITheatreService {

	TheatreDto addTheatre(TheatreDto theatre);

	TheatreDto updateTheatre(Long theatreId, TheatreDto theatre);

	void deleteTheatre(Long theatreId);

	TheatreDto getTheatreById(Long theatreId);

	List<TheatreDto> getAllTheatres();

	List<TheatreDto> getTheatresByCity(String city);
	
    TheatreDto saveTheatre(TheatreDto dto);

}