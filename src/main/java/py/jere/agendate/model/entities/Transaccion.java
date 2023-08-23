package py.jere.agendate.model.entities;

import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.model.ModelCustom;
import py.jere.agendate.model.enums.TipoBeneficio;
import py.jere.agendate.model.enums.TipoDescuento;
import py.jere.agendate.model.enums.TipoTransaccion;

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
public class Transaccion extends ModelCustom<Usuario> {

	@Enumerated(EnumType.STRING)
	private TipoTransaccion tipo; // VENTA
	private double total; // sumatoria - descuento
	private double descuento; // Ademas del descuento que otorga un grupo
	private double sumatoria; // Todos los items
	private boolean aplicarPromocion;
	@Enumerated(EnumType.STRING)
	private TipoBeneficio tipoBeneficio;
	@Enumerated(EnumType.STRING)
	private TipoDescuento tipoDescuento;
	@ManyToOne(optional = false)
	private Persona persona;
	@ManyToOne(optional = true)
	private Grupo grupo;
	@ManyToOne(optional = true)
	private Beneficio beneficio;
	@OneToMany(mappedBy = "transaccion")
	private List<TransaccionDetalle> detalles;
}