package com.cg.service;

import com.cg.dto.TheatreDto;
import com.cg.entity.Theatre;
import com.cg.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheatreService implements ITheatreService {

	@Autowired
	private TheatreRepository theatreRepository;

	public TheatreService(TheatreRepository theatreRepository) {
		this.theatreRepository = theatreRepository;
	}

	// ========== CREATE ==========
	@Override
	public TheatreDto addTheatre(TheatreDto dto) {
		Theatre entity = toEntity(dto);
		Theatre saved = theatreRepository.save(entity);
		return toDto(saved);
	}

	// ========== UPDATE ==========
	@Override
	public TheatreDto updateTheatre(Long theatreId, TheatreDto updated) {
		Theatre t = theatreRepository.findById(theatreId).orElseThrow(() -> new RuntimeException("Theatre not found"));

		// Apply updatable fields
		t.setTheatreName(updated.getTheatreName());
		t.setLocation(updated.getLocation());
		t.setCity(updated.getCity());
		t.setTotalSeats(updated.getTotalSeats());
		t.setContactNumber(updated.getContactNumber());

		Theatre saved = theatreRepository.save(t);
		return toDto(saved);
	}

	// ========== SAVE (Create or Partial Update) ==========
	@Override
	public TheatreDto saveTheatre(TheatreDto dto) {
		Theatre entity;
		if (dto.getTheatreId() != null) {
			entity = theatreRepository.findById(dto.getTheatreId())
					.orElseThrow(() -> new RuntimeException("Theatre not found: " + dto.getTheatreId()));

			if (dto.getTheatreName() != null)
				entity.setTheatreName(dto.getTheatreName());
			if (dto.getLocation() != null)
				entity.setLocation(dto.getLocation());
			if (dto.getCity() != null)
				entity.setCity(dto.getCity());
			if (dto.getTotalSeats() != -1)
				entity.setTotalSeats(dto.getTotalSeats()); // Integer
			if (dto.getContactNumber() != null)
				entity.setContactNumber(dto.getContactNumber());
		} else {
			entity = toEntity(dto); // on create, you may still want to validate required fields
		}

		return toDto(theatreRepository.save(entity));
	}

	// ========== DELETE ==========
	@Override
	public void deleteTheatre(Long theatreId) {
		theatreRepository.deleteById(theatreId);
	}

	// ========== READ: BY ID ==========
	@Override
	public TheatreDto getTheatreById(Long theatreId) {
		Theatre t = theatreRepository.findById(theatreId).orElseThrow(() -> new RuntimeException("Theatre not found"));
		return toDto(t);
	}

	// ========== READ: ALL ==========
	@Override
	public List<TheatreDto> getAllTheatres() {
		return theatreRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	// ========== READ: BY CITY ==========
	@Override
	public List<TheatreDto> getTheatresByCity(String city) {
		return theatreRepository.findByCityIgnoreCase(city).stream().map(this::toDto).collect(Collectors.toList());
	}

	// ===== Manual mapping helpers =====
	private TheatreDto toDto(Theatre t) {
		if (t == null)
			return null;
		TheatreDto dto = new TheatreDto();
		dto.setTheatreId(t.getTheatreId());
		dto.setTheatreName(t.getTheatreName());
		dto.setLocation(t.getLocation());
		dto.setCity(t.getCity());
		dto.setTotalSeats(t.getTotalSeats());
		dto.setContactNumber(t.getContactNumber());
		return dto;
	}

	private Theatre toEntity(TheatreDto dto) {
		if (dto == null)
			return null;
		Theatre t = new Theatre();
		t.setTheatreId(dto.getTheatreId());
		t.setTheatreName(dto.getTheatreName());
		t.setLocation(dto.getLocation());
		t.setCity(dto.getCity());
		t.setTotalSeats(dto.getTotalSeats());
		t.setContactNumber(dto.getContactNumber());
		return t;
	}
}