package py.jere.agendate.model.entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table
@DynamicInsert
@DynamicUpdate
public class TransaccionDetalle extends ModelCustom<User> {
	private double valor;
	private double cantidad;
	private double subtotal;
	@ManyToOne
	private Transaccion transaccion;
	@ManyToOne
	private Item item;

}
