package py.podac.tech.agenda.model.entities;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.podac.tech.agenda.controller.utils.Edad;
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.security.user.User;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table
@DynamicUpdate
public class Persona extends ModelCustom<User> {

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate fechaNacimiento;

	@Transient
	private int edad;

	@Column(nullable = false)
	private String genero;

	@Column(nullable = false, unique = true)
	private String documentoIdentidad;

	@Column
	private String telefono;

	@Column
	private String celular;

	@Column
	private String correo;

	@Column
	private String direccion;

	@Column
	private String observacion;

	@Column
	private String fotoPerfil;

	// La entidad Persona mapea en Colaborador
	// a traves del campo 'persona'
	@OneToOne(mappedBy = "persona")
	@JsonManagedReference
	private Colaborador colaborador;

	@OneToOne(mappedBy = "persona")
	@JsonManagedReference
	private User user;

	public int getEdad() {
		return Edad.calcular(fechaNacimiento);
	}
}
