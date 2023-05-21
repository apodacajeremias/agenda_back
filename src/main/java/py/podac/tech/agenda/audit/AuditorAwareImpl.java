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
		System.out.println(auth.getPrincipal());
		if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
			return Optional.empty();
		}
		final User USUARIO = (User) auth.getPrincipal();
		if (USUARIO == null) {
			return Optional.empty();
		}
		return Optional.of(USUARIO);
	}

}
