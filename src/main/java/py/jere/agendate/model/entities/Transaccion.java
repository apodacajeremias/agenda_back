package py.jere.agendate.model.entities;

import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
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
import py.jere.agendate.security.user.User;

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

	@Enumerated(EnumType.STRING)
	private TipoTransaccion tipo; // VENTA
	private double total; // sumatoria - descuento
	private double descuento; // Ademas del descuento que otorga un grupo
	private double sumatoria; // Todos los items
	private boolean aplicarPromocion;
	@Enumerated(EnumType.STRING)
	private TipoBeneficio tipoBeneficio; // Definido por Grupo
	@Enumerated(EnumType.STRING)
	private TipoDescuento tipoDescuento; // Definido por Grupo
	@ManyToOne(optional = false, cascade = CascadeType.MERGE) @JsonBackReference
	private Persona persona;
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	private Grupo grupo;
	@ManyToOne(optional = true, cascade = CascadeType.MERGE)
	private Beneficio beneficio;
	@OneToMany(mappedBy = "transaccion")
	private List<TransaccionDetalle> detalles;
	
	@Override
	public String getNombre() {
		if(persona != null) {
			return this.persona.getNombre();
		}
		return super.getNombre();
	}
}
