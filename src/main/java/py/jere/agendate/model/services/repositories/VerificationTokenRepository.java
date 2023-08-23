package py.jere.agendate.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.jere.agendate.model.entities.Usuario;
import py.jere.agendate.security.auth.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

	VerificationToken findByToken(String token);

	VerificationToken findByUsuario(Usuario usuario);

}
