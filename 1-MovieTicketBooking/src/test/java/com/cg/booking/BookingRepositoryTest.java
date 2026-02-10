package com.cg.booking;

import com.cg.entity.Booking;
import com.cg.entity.Movie;
import com.cg.entity.Role;
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.entity.Theatre;
import com.cg.entity.User;
import com.cg.repository.BookingRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
// Force Hibernate to generate schema in this slice test
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.format_sql=true"
})
// 💡 Force entity and repository scanning by type (compile-safe)
@EntityScan(basePackageClasses = {
        User.class, Movie.class, Theatre.class, Show.class, Seat.class, Booking.class
        // Add Payment.class or BookingSeat.class here if those are entities
})
@EnableJpaRepositories(basePackageClasses = {
        BookingRepository.class
})
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void saveBooking_success() {
        // ---- USER ----
        User user = new User();
        user.setUsername("anshu");
        user.setEmail("a@test.com");
        user.setPassword("12345");
        user.setRole(Role.USER);
        user.setPhoneNumber("9999999999");
        user.setEnabled(true);
        em.persistAndFlush(user);

        // ---- MOVIE ----
        Movie movie = new Movie();
        movie.setMovieName("Test");
        movie.setGenre("Action");
        movie.setLanguage("English");
        movie.setDuration(120);
        movie.setDescription("desc");
        movie.setRating(8.0);
        movie.setPosterUrl("url");
        em.persistAndFlush(movie);

        // ---- THEATRE ----
        Theatre theatre = new Theatre();
        theatre.setTheatreName("PVR");
        em.persistAndFlush(theatre);

        // ---- SHOW ----
        Show show = new Show();
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(LocalDate.now());
        show.setShowTime(LocalTime.NOON);
        show.setPrice(200);
        em.persistAndFlush(show);

        // ---- BOOKING ----
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setPaymentStatus("PAID");
        booking.setBookingStatus("CONFIRMED");
        booking.setBookingDate(LocalDate.now());
        booking.setTotalAmount(200);

        var saved = bookingRepository.saveAndFlush(booking);
        assertNotNull(saved.getBookingId());
    }
}



























/*
 * package com.cg.booking;
 * 
 * import com.cg.entity.*; import com.cg.repository.BookingRepository;
 * 
 * import org.junit.jupiter.api.Test; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.autoconfigure.domain.EntityScan; import
 * org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
 * import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
 * import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
 * import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
 * import org.springframework.test.context.TestPropertySource;
 * 
 * import java.time.LocalDate; import java.time.LocalTime;
 * 
 * import static org.junit.jupiter.api.Assertions.*;
 * 
 * @DataJpaTest
 * 
 * @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
 * 
 * @TestPropertySource(properties = {
 * "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=true" })
 * //@EntityScan("com.cg.entity") //@EnableJpaRepositories("com.cg.repository")
 * class BookingRepositoryTest {
 * 
 * @Autowired private TestEntityManager em;
 * 
 * @Autowired private BookingRepository bookingRepository;
 * 
 * @Test void saveBooking_success() {
 * 
 * User u = new User(); u.setUsername("anshu"); u.setEmail("a@test.com");
 * u.setPassword("12345"); u.setRole(Role.USER); u.setPhoneNumber("9999999999");
 * u.setEnabled(true); em.persist(u);
 * 
 * Movie m = new Movie(); m.setMovieName("Test"); m.setGenre("Action");
 * m.setLanguage("English"); m.setDuration(120); m.setDescription("desc");
 * m.setRating(8.0); m.setPosterUrl("url"); em.persist(m);
 * 
 * Theatre t = new Theatre(); t.setTheatreName("PVR"); em.persist(t);
 * 
 * Show s = new Show(); s.setMovie(m); s.setTheatre(t);
 * s.setShowDate(LocalDate.now()); s.setShowTime(LocalTime.NOON);
 * s.setPrice(200); em.persist(s);
 * 
 * Booking b = new Booking(); b.setUser(u); b.setShow(s);
 * b.setPaymentStatus("PAID"); b.setBookingStatus("CONFIRMED");
 * b.setBookingDate(LocalDate.now()); b.setTotalAmount(200);
 * 
 * Booking saved = bookingRepository.saveAndFlush(b);
 * 
 * assertNotNull(saved.getBookingId()); } }
 */