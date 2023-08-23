package py.jere.agendate.model.services.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import py.jere.agendate.model.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
	Optional<Usuario> findByEmail(String email);

	boolean existsByEmail(String email);

	List<Usuario> findByEnabledIsTrue();

	List<Usuario> findByEnabledIsFalse();
	
	@Modifying
	@Transactional
	@Query("UPDATE Usuario u SET u.enabled = true WHERE u.id = :id")
	void enableUsuario(@Param("id") UUID ID);
}
