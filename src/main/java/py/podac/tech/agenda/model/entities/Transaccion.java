package py.podac.tech.agenda.model.entities;

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
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.model.enums.TipoBeneficio;
import py.podac.tech.agenda.model.enums.TipoDescuento;
import py.podac.tech.agenda.model.enums.TipoTransaccion;
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
public class Transaccion extends ModelCustom<User> {
	@Default
	@Enumerated(EnumType.STRING)
	private TipoTransaccion tipo = TipoTransaccion.VENTA;
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
