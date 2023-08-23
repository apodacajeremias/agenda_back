package py.jere.agendate.model.entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.model.ModelCustom;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
public class Colaborador extends ModelCustom<Usuario> {

	@NotNull
	@NotEmpty
	@Column
	private String registroContribuyente;

	@Column
	private String registroProfesional;

	@Column
	private String profesion;

}