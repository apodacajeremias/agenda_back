package py.podac.tech.agenda.model.services.interfaces;

import java.time.LocalDateTime;

import py.podac.tech.agenda.model.entities.Agenda;
import py.podac.tech.agenda.model.services.ServiceCustom;

public interface IAgendaService extends ServiceCustom<Agenda> {
	boolean horarioDisponible(LocalDateTime inicio, LocalDateTime fin);
}
