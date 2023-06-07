package py.podac.tech.agenda.model.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import py.podac.tech.agenda.model.ModelCustom;
import py.podac.tech.agenda.model.enums.TipoDescuento;
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
public class Promocion extends ModelCustom<User> {
	private LocalDate inicio;
	private LocalDate fin;
	private double valor;
	private TipoDescuento tipoDescuento;
	@ManyToMany(mappedBy = "promociones")
	private List<Beneficio> beneficios;

}
