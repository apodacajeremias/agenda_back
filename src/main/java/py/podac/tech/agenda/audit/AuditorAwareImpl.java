package py.podac.tech.agenda.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import py.podac.tech.agenda.model.entities.Usuario;

@Component
public class AuditorAwareImpl implements AuditorAware<Usuario> {

	@Override
	public Optional<Usuario> getCurrentAuditor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
			return Optional.empty();
		}
		final Usuario USUARIO = (Usuario) auth.getPrincipal();
		if (USUARIO == null) {
			return Optional.empty();
		}
		return Optional.of(USUARIO);
	}

}
