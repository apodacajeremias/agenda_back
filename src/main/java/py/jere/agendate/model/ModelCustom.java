package py.jere.agendate.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ModelCustom<T> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(updatable = false)
	@ReadOnlyProperty
	private UUID id;

	@Column(nullable = false)
	@JsonProperty("activo")
	private boolean activo;

	@Column(nullable = false)
	@JsonProperty("nombre")
	private String nombre;

	@CreatedBy
	@ManyToOne
	@JoinColumn(updatable = false)
	@ReadOnlyProperty
	@JsonIgnore
	private T creadoPor;

	@LastModifiedBy
	@ManyToOne
	@ReadOnlyProperty
	@JsonIgnore
	private T modificadoPor;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	@ReadOnlyProperty
	private LocalDateTime fechaCreacion;

	@LastModifiedDate
	@Column(nullable = true)
	@ReadOnlyProperty
	private LocalDateTime fechaModificacion;

}
