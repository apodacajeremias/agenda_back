package py.podac.tech.agenda.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import py.podac.tech.agenda.model.entities.Usuario;
import py.podac.tech.agenda.security.auth.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

	VerificationToken findByToken(String token);

	VerificationToken findByUsuario(Usuario usuario);

}
