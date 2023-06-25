package py.podac.tech.agenda.model;

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
import lombok.Builder.Default;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ToString
public abstract class ModelCustom<T> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(updatable = false)
	private UUID ID;

	@Column(nullable = false)
	@Default
	private boolean activo = true;

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

	public ModelCustom(UUID ID) {
		this.ID = ID;
	}

	public UUID getID() {
		return ID;
	}

	public void setID(UUID iD) {
		ID = iD;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public T getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(T creadoPor) {
		this.creadoPor = creadoPor;
	}

	public T getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(T modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}
