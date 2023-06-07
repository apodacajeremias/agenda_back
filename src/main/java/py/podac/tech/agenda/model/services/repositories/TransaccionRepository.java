package py.podac.tech.agenda.model.services.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.podac.tech.agenda.model.entities.Transaccion;
import py.podac.tech.agenda.model.services.RepositoryCustom;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, UUID>, RepositoryCustom<Transaccion> {

}
