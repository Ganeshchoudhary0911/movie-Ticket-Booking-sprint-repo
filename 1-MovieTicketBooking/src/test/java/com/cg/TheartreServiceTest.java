package com.cg;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.cg.entity.Theatre;
import com.cg.service.TheatreService;
import jakarta.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TheatreServiceTest {

    @Autowired
    private TheatreService theatreService;
    
    @BeforeAll
	public static void init() {
		System.out.println("Theatre Test Cases");
	}

    @Test
    public void testAddTheatre() {
        Theatre theatre = new Theatre("PVR", "Delhi");
        Theatre saved = theatreService.save(theatre);

        assertNotNull(saved.getTheatreId(), "Theatre ID should not be null");
    }

    @Test
    void shouldReturnAllTheatres() {
        // 1. Get the count before adding anything
        int countBefore = theatreService.getAllTheatres().size();

        // 2. Add your test data
        theatreService.save(new Theatre("PVR", "Delhi"));
        theatreService.save(new Theatre("INOX", "Mumbai"));

        // 3. Assert that it is count + 2
        List<Theatre> theatres = theatreService.getAllTheatres();
        assertEquals(countBefore + 2, theatres.size(), "The count should increase by exactly 2");
    }

    @Test
    void shouldDeleteTheatre() {
        // 1. Save a theatre
        Theatre t = theatreService.save(new Theatre("PVR", "Delhi"));
        Long id = t.getTheatreId();

        // 2. Delete it
        theatreService.deleteTheatre(id);

        // 3. FIX: Check that THIS specific theatre is gone
        // We use assertThrows because your getTheatreById throws a RuntimeException if not found
        assertThrows(RuntimeException.class, () -> {
            theatreService.getTheatreById(id);
        }, "Should throw exception as theatre should be deleted");
    }

}