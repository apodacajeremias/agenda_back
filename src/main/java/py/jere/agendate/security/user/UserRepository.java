package py.jere.agendate.security.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  
  boolean existsByEmail(String email);

	List<User> findByEnabledIsTrue();

	List<User> findByEnabledIsFalse();
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.enabled = true WHERE u.id = :id")
	void enableUser(@Param("id") UUID id);

}
