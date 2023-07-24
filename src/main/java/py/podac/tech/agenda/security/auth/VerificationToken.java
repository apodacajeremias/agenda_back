package py.podac.tech.agenda.security.auth;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.podac.tech.agenda.model.entities.Usuario;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VerificationToken {
	@Transient
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID ID;

	private String token;

	@OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private Usuario usuario;

	private Date expiryDate;

	public VerificationToken(String token, Usuario usuario) {
		super();
		this.token = token;
		this.usuario = usuario;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	public void updateToken(final String token) {
		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	public Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}
}