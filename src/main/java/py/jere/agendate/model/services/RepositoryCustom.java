package py.jere.agendate.model.services;

import java.util.List;
import java.util.Optional;

public interface RepositoryCustom<T> {
	List<T> findByActivoIsTrue();
	List<T> findByActivoIsFalse();
	Optional<T> findTopByOrderByFechaCreacionDesc();
}
