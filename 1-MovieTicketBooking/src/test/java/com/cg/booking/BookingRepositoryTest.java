package com.cg.booking;

import com.cg.entity.*;
import com.cg.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EntityScan("com.cg.entity")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=true"
})
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void saveBooking_success() {
        User u = new User();
        u.setUsername("anshu");
        u.setEmail("a@test.com");
        u.setPassword("12345");
        u.setRole(Role.USER);
        u.setPhoneNumber("9876543213");
        u.setEnabled(true);
        em.persist(u);

        Movie m = new Movie();
        m.setMovieName("Test");
        m.setGenre("Action");
        m.setLanguage("English");
        m.setDuration(120);
        m.setDescription("desc");
        m.setRating(8.0);
        m.setPosterUrl("url.com");
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
        b.setPaymentStatus("PAID");
        b.setBookingStatus("CONFIRMED");
        b.setBookingDate(LocalDate.now());
        b.setTotalAmount(200);

        Booking saved = bookingRepository.saveAndFlush(b);
        assertNotNull(saved.getBookingId());
    }
}