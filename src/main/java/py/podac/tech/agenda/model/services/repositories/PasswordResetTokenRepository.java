package py.podac.tech.agenda.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.reset.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

	PasswordResetToken findByToken(String token);

	PasswordResetToken findByUser(User user);

}
