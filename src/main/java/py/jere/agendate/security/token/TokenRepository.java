package py.jere.agendate.security.token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
	Optional<Token> findByToken(String token);
	List<Token> findByUserId(UUID id);
	List<Token> findByUserIdAndRevokedIsFalseAndExpiredIsFalse(UUID id);
	List<Token> findByUserIdAndTypeAndRevokedIsFalseAndExpiredIsFalse(UUID id, TokenType type);
	List<Token> findByRevokedIsFalseAndExpiredIsFalse();
	List<Token> findByRevokedIsTrueOrExpiredIsTrue();
	@Modifying
	@Transactional
	@Query("UPDATE Token t SET t.revoked = true, t.expired = true WHERE t.user.id = :id AND type = :type")
	void revokeAndExpireByIdAndType(@Param(value = "id") UUID id, @Param(value = "type") TokenType type);
}
