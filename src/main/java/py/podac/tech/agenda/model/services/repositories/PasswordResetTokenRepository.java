package py.podac.tech.agenda.model.services.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.reset.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

	Optional<PasswordResetToken> findByToken(String token);

	Optional<PasswordResetToken> findByUser(User user);

}
