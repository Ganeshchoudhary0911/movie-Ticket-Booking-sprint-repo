package com.cg.theatre;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cg.entity.Theatre;
import com.cg.repository.TheatreRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TheatreRepositoryTest {

    @Autowired
    private TheatreRepository theatreRepository;

    @Test
    void testSaveAndFindTheatre() {
        Theatre theatre = new Theatre("PVR", "City Center");
        Theatre saved = theatreRepository.save(theatre);
        assertNotNull(saved.getTheatreId());

        Optional<Theatre> found = theatreRepository.findById(saved.getTheatreId());
        assertTrue(found.isPresent());
        assertEquals("PVR", found.get().getTheatreName());
    }

    @Test
    void testFindAllTheatres() {
        theatreRepository.save(new Theatre("PVR", "City Center"));
        List<Theatre> theatres = theatreRepository.findAll();
        assertFalse(theatres.isEmpty());
    }

    @Test
    void testDeleteTheatre() {
        Theatre theatre = theatreRepository.save(new Theatre("PVR", "City Center"));
        theatreRepository.deleteById(theatre.getTheatreId());
        Optional<Theatre> deleted = theatreRepository.findById(theatre.getTheatreId());
        assertFalse(deleted.isPresent());
    }
}