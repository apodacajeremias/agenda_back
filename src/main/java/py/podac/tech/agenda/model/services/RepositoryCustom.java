package py.podac.tech.agenda.model.services;

import java.util.List;

public interface RepositoryCustom<T> {
	List<T> findByActivoIsTrue();
	List<T> findByActivoIsFalse();
	T findTopByOrderByFechaCreacionDesc();
}
