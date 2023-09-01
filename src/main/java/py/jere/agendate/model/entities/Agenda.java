package py.jere.agendate.model.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.model.ModelCustom;
import py.jere.agendate.model.enums.Prioridad;
import py.jere.agendate.model.enums.Situacion;
import py.jere.agendate.security.user.User;

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

	@Column(nullable = false)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime inicio;

	@Column(nullable = false)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fin;
	
	@Column
	private boolean diaCompleto;

	@Column
	@Enumerated(EnumType.STRING)
	private Situacion situacion;

	@Column
	@Enumerated(EnumType.STRING)
	private Prioridad prioridad;

	@ManyToOne(optional = false, cascade = CascadeType.MERGE)
	private Colaborador colaborador;

	@ManyToOne(optional = false, cascade = CascadeType.MERGE)
	private Persona persona;
	

}
