package py.podac.tech.agenda.model.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import py.podac.tech.agenda.model.enums.TokenType;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID ID;

	@Column(unique = true)
	public String token;

	@Default
	@Enumerated(EnumType.STRING)
	public TokenType tokenType = TokenType.BEARER;

	public boolean revoked;

	public boolean expired;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonBackReference
	@ToString.Exclude
	public Usuario usuario;
}
