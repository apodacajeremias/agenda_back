package py.podac.tech.agenda.model.services.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.podac.tech.agenda.model.services.RepositoryCustom;
import py.podac.tech.agenda.security.user.User;

public interface UserRepository extends JpaRepository<User, UUID>, RepositoryCustom<User> {
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}
