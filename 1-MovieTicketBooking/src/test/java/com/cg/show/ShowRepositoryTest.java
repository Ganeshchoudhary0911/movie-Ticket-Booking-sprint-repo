package com.cg.show;

import com.cg.entity.*;
import com.cg.repository.ShowRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShowRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ShowRepository showRepository;

	@Test
	void testFindAllShows() {

		Movie movie = entityManager.persist(new Movie(null, "RRR"));
		Theatre theatre = entityManager.persist(new Theatre(null, "INOX"));

		Show show = new Show(null, LocalDate.now(), LocalTime.now(), 250, movie, theatre);

		entityManager.persist(show);

		assertThat(showRepository.findAll()).hasSize(1);
	}
}
