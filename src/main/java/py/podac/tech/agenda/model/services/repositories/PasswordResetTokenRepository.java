package py.podac.tech.agenda.model.services.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.podac.tech.agenda.model.entities.Usuario;
import py.podac.tech.agenda.security.auth.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

	Optional<PasswordResetToken> findByToken(String token);

	Optional<PasswordResetToken> findByUser(Usuario usuario);

}
