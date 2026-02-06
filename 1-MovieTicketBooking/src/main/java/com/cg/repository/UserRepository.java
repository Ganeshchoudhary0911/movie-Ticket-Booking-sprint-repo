package com.cg.repository;

import com.cg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
<<<<<<< HEAD
	 
	    User findByEmail(String email);
		User findByUsername(String username);
		boolean existsByUsername(String username);
		boolean existsByEmail(String email);

		
		
	}
	 

=======
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
>>>>>>> a948f28cc119208da9844bcfe3de64e490875643
