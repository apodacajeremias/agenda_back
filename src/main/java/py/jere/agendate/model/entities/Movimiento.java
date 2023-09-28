package py.jere.agendate.model.entities;

import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.jere.agendate.model.ModelCustom;
import py.jere.agendate.model.enums.MedioPago;
import py.jere.agendate.model.enums.Moneda;
import py.jere.agendate.model.enums.TipoMovimiento;
import py.jere.agendate.security.user.User;

// Movimiento de dinero: Ingreso o Egreso

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
public class Movimiento extends ModelCustom<User> {
	private double valor;
	private String numeroComprobante;
	private Moneda moneda;
	private TipoMovimiento tipo; // INGRESO = Pago
	private MedioPago medioPago;
	@OneToMany(mappedBy = "movimiento")
	private List<MovimientoDetalle> detalles;

}
