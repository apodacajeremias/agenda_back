package py.podac.tech.agenda.model.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import py.podac.tech.agenda.model.enums.Rol;
import py.podac.tech.agenda.model.interfaces.ValidEmail;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@DynamicInsert
@DynamicUpdate
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID ID;

	@ValidEmail
	private String email;

	private String password;

	@Transient
	private String matchingPassword;

	private boolean changePassword;

	private boolean enabled;

	private LocalDate lastPasswordChange;

	@Enumerated(EnumType.STRING)
	private Rol rol;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.DETACH })
	@ToString.Exclude
	private Persona persona;

	@OneToMany(mappedBy = "usuario")
	@JsonManagedReference
	@ToString.Exclude
	private List<Token> tokens;

	public Usuario(UUID iD) {
		super();
		ID = iD;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(rol.name()));
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
