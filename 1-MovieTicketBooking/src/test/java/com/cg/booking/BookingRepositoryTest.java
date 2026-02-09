package com.cg.booking;

import com.cg.entity.*;
import com.cg.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void saveBooking_success() {

        User u = new User();
        u.setUsername("anshu");
        u.setEmail("a@test.com"); // important
        em.persist(u);

        Movie m = new Movie();
        m.setMovieName("Test");
        m.setGenre("Action");
        m.setLanguage("English");
        m.setDuration(120);
        m.setDescription("desc");
        m.setRating(8.0);
        m.setPosterUrl("url");
        em.persist(m);

        Theatre t = new Theatre();
        t.setTheatreName("PVR");
        em.persist(t);

        Show s = new Show();
        s.setMovie(m);
        s.setTheatre(t);
        s.setShowDate(LocalDate.now());
        s.setShowTime(LocalTime.NOON);
        s.setPrice(200);
        em.persist(s);

        Booking b = new Booking();
        b.setUser(u);
        b.setShow(s);
        b.setBookingStatus("CONFIRMED");
        b.setPaymentStatus("PAID");

        Booking saved = bookingRepository.save(b);

        assertNotNull(saved.getBookingId());
    }
}

