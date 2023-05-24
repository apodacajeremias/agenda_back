package py.podac.tech.agenda.model.services.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import py.podac.tech.agenda.security.token.Token;

public interface TokenRepository extends JpaRepository<Token, UUID> {

	@Query(value = """
			select t from Token t inner join User u\s
			on t.user.ID = u.ID\s
			where u.ID = :ID and (t.expired = false or t.revoked = false)\s
			""")
	List<Token> findAllValidTokenByUser(UUID ID);

	Optional<Token> findByToken(String token);

	Optional<Token> findByTokenAndRevokedIsFalseAndExpiredIsFalse(String token);
}
