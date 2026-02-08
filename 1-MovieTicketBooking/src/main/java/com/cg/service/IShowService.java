package com.cg.service;

import com.cg.dto.ShowDto;
import java.util.List;
	 
public interface IShowService {
	 

	// Create a show using DTO (expects movieId & theatreId inside the DTO)
	    ShowDto addShow(ShowDto showDto);

	    // Update a show by id using DTO
	    ShowDto updateShow(Long id, ShowDto showDto);

	    // Delete by id
	    void deleteShow(Long id);

	    // Get single show by id as DTO
	    ShowDto getShowById(Long id);

	    // Get all shows as DTOs
	    List<ShowDto> getAllShows();

	}

