package py.jere.agendate.model.entities;

import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
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
public class Beneficio extends ModelCustom<User> {
	
	@Enumerated(EnumType.STRING)
	private TipoBeneficio tipo; //FAMILIAR
	@Enumerated(EnumType.STRING)
	private TipoDescuento tipoDescuento; //VALOR
	private double descuento;
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "beneficios")
	private List<Promocion> promociones;

}
