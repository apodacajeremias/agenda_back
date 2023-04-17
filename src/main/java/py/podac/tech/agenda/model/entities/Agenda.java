package py.podac.tech.agenda.model.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.model.enums.Prioridad;
import py.podac.tech.agenda.model.enums.Situacion;
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
public class Agenda extends ModelCustom<User> {

	@Column
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate fecha;

	@Column
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime hora;

	@Column
	@Enumerated(EnumType.STRING)
	@Default
	private Situacion situacion = Situacion.PENDIENTE;

	@Column
	@Enumerated(EnumType.STRING)
	@Default
	private Prioridad prioridad = Prioridad.BAJA;

	@ManyToOne(optional = false)
	private Colaborador colaborador;

	@ManyToOne(optional = false)
	private Persona persona;

}
