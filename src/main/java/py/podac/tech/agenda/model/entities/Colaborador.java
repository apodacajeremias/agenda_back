package py.podac.tech.agenda.model.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.security.user.User;

@Data @SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity @Table
@DynamicUpdate
public class Colaborador extends ModelCustom<User> {
	
	@Column(nullable = false, unique = true)
	private String registroContribuyente;
	
	@Column
	private String registroProfesional;
	
	@Column
	private String profesion;
	
	@OneToOne @JoinColumn
	@JsonBackReference
	private Persona persona;
	
	

	

		
}
