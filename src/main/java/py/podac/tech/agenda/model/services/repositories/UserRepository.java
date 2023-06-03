package py.podac.tech.agenda.model.services.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.podac.tech.agenda.security.user.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	List<User> findByEnabledIsTrue();

	List<User> findByEnabledIsFalse();
}
