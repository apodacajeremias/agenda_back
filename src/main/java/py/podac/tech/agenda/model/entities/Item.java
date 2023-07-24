package py.podac.tech.agenda.model.entities;

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
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.model.enums.TipoTransaccion;

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
public class Item extends ModelCustom<Usuario> {
	private double precio;
	@Enumerated(EnumType.STRING)
	private TipoTransaccion tipo; // VENTA
}
