package py.podac.tech.agenda.model.entities;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.model.enums.Idioma;
import py.podac.tech.agenda.model.enums.Moneda;
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
public class Empresa extends ModelCustom<User> {

	private String celular;
	private String telefono;
	private LocalDate fechaInauguracion; // Ej: 'Desde 2001' para reportes
	private String direccion;
	private String registroContribuyente; // Para emision de facturas y notas
	@Default @Enumerated(EnumType.STRING)
	private Moneda moneda = Moneda.GUARANI;
	@Default @Enumerated(EnumType.STRING)
	private Idioma idioma = Idioma.CASTELLANO;
	private String logo;

}
