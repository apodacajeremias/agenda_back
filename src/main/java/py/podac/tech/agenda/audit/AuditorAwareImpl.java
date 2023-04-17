package py.podac.tech.agenda.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import py.podac.tech.agenda.security.user.User;

@Component
public class AuditorAwareImpl implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User USUARIO = (User) auth.getPrincipal();
		if (auth == null || !auth.isAuthenticated() || USUARIO == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(USUARIO);
	}

}
