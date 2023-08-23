package py.jere.agendate.model.entities;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.model.ModelCustom;
import py.jere.agendate.model.enums.Idioma;
import py.jere.agendate.model.enums.Moneda;

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
public class Empresa extends ModelCustom<Usuario> {

	private String celular;
	private String telefono;
	private LocalDate fechaInauguracion; // Ej: 'Desde 2001' para reportes
	private String direccion;
	private String registroContribuyente; // Para emision de facturas y notas
	private String logo;
	@Enumerated(EnumType.STRING)
	private Moneda moneda; // GUARANI
	@Enumerated(EnumType.STRING)
	private Idioma idioma; // CASTELLANO

}
