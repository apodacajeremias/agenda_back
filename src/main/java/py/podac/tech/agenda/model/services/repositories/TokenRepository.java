package py.podac.tech.agenda.model.services.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import py.podac.tech.agenda.model.entities.Token;

public interface TokenRepository extends JpaRepository<Token, UUID> {

	@Query(value = "SELECT t FROM Token t INNER JOIN Usuario u on t.usuario.ID = u.ID WHERE u.ID = :ID and (t.expired = false OR t.revoked = false)")
	List<Token> findAllValidTokenByUser(UUID ID);

	Optional<Token> findByToken(String token);

	Optional<Token> findByTokenAndRevokedIsFalseAndExpiredIsFalse(String token);
}
