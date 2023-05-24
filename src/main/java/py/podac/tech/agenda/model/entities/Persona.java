package py.podac.tech.agenda.model.entities;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.podac.tech.agenda.controller.utils.Edad;
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.model.enums.Genero;
import py.podac.tech.agenda.model.interfaces.ValidEmail;
import py.podac.tech.agenda.security.user.User;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class Persona extends ModelCustom<User> {

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String nombre;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate fechaNacimiento;

	@Transient
	private int edad;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	@Default
	private Genero genero = Genero.OTRO;

	@NotNull
	@NotEmpty
	@Column(nullable = false, unique = true)
	private String documentoIdentidad;

	@Column
	private String telefono;

	@Column
	private String celular;

	@Column
	@ValidEmail
	private String correo;

	@Column
	private String direccion;

	@Column
	private String observacion;

	@Column
	private String fotoPerfil;

	@OneToOne(cascade = CascadeType.ALL)
	private Colaborador colaborador;

	@OneToOne(cascade = CascadeType.ALL)
	private User user;

	public Persona(UUID ID) {
		super(ID);
	}

	public int getEdad() {
		return Edad.calcular(fechaNacimiento);
	}
}
