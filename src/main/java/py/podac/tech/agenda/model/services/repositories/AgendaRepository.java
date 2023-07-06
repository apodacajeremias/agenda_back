package py.podac.tech.agenda.model.services.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.podac.tech.agenda.model.entities.Agenda;
import py.podac.tech.agenda.model.services.RepositoryCustom;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, UUID>, RepositoryCustom<Agenda> {

	boolean existsByFechaBetweenAndHoraBetweenAndActivoIsTrue(LocalDate fechaInicial, LocalDate fechaFinal, LocalTime horaInicial, LocalTime horaFinal);
}