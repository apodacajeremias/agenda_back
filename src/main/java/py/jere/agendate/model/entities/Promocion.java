package py.jere.agendate.model.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.model.ModelCustom;
import py.jere.agendate.model.enums.TipoDescuento;

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
public class Promocion extends ModelCustom<Usuario> {
	private LocalDate inicio;
	private LocalDate fin;
	private double valor;
	private TipoDescuento tipoDescuento;
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH})
	private List<Beneficio> beneficios;

}
