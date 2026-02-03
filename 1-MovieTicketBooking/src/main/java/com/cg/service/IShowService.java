package com.cg.service;

import com.cg.entity.Show;
import java.util.List;
	 
public interface IShowService {
	 
	    Show addShow(Show show);
	 
	    Show updateShow(Long id, Show show);
	 
	    void deleteShow(Long id);
	 
	    Show getShowById(Long id);
	 
	    List<Show> getAllShows();
	}

