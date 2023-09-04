package py.jere.agendate.security.user;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import py.jere.agendate.model.entities.Persona;
import py.jere.agendate.model.interfaces.ValidEmail;
import py.jere.agendate.security.token.Token;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@DynamicInsert
@DynamicUpdate
public class User implements UserDetails {

	private static final long serialVersionUID = -2674435762543042346L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ValidEmail
	private String email;
	private String password;
	private boolean changePassword;
	private LocalDate lastPasswordChange;
	@Enumerated(EnumType.STRING)
	private Role role;
	private boolean enabled;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.DETACH })
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Persona persona;

	@OneToMany(mappedBy = "user")
	private List<Token> tokens;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled ? true : true;
	}
}