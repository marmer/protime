package io.github.marmer.protim.repositories;

import io.github.marmer.protim.model.Useraccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Interface UseraccountRepository.
 *
 * @author marmer
 * @since 03.10.2017
 */
public interface UseraccountRepository extends JpaRepository<Useraccount, Long> {
	/**
	 * Find by username.
	 *
	 * @param username the username
	 * @return the useraccount
	 */
	Useraccount findByUsername(String username);
}
