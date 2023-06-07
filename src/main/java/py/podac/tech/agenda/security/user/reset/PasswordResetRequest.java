package py.podac.tech.agenda.security.user.reset;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.podac.tech.agenda.model.interfaces.PasswordMatches;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class PasswordResetRequest {

	@NotNull
	@NotEmpty
	private String token;

	@NotNull
	@NotEmpty
	private String password;

	@NotNull
	@NotEmpty
	private String matchingPassword;
}