package py.jere.agendate.model.services.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.jere.agendate.model.entities.Usuario;
import py.jere.agendate.security.auth.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

	Optional<PasswordResetToken> findByToken(String token);

	Optional<PasswordResetToken> findByUsuario(Usuario usuario);

}
