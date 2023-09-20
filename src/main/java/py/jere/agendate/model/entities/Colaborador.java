package py.jere.agendate.model.entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.model.ModelCustom;
import py.jere.agendate.security.user.User;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
public class Colaborador extends ModelCustom<User> {

	@NotNull
	@NotEmpty
	@Column
	private String registroContribuyente;

	@Column
	private String registroProfesional;

	@Column
	private String profesion;
	
	@OneToOne(mappedBy = "colaborador", cascade = CascadeType.MERGE)
	@JsonBackReference
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Persona persona;
	
	@Override
	public String getNombre() {
		if(this.persona != null) {
			return this.persona.getNombre();			
		}
		return null;
	}

}
