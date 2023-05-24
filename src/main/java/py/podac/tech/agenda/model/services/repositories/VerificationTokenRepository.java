package py.podac.tech.agenda.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

	VerificationToken findByToken(String token);

	VerificationToken findByUser(User user);

}
