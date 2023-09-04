package py.jere.agendate.model.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.controller.utils.Edad;
import py.jere.agendate.model.ModelCustom;
import py.jere.agendate.model.enums.Genero;
import py.jere.agendate.security.user.User;

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
	@Column(nullable = false, unique = true)
	private String documentoIdentidad;

	@Column(nullable = false)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDate fechaNacimiento;

	@Transient
	private int edad;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Genero genero;

	@Column
	private String telefono;

	@Column
	private String celular;

	@Column
	private String direccion;

	@Column
	private String observacion;

	@Column
	private String fotoPerfil;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonManagedReference
	private Colaborador colaborador;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonManagedReference
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH }, mappedBy = "personas")
	private List<Grupo> grupos;

	public int getEdad() {
		return Edad.calcular(fechaNacimiento);
	}
}
