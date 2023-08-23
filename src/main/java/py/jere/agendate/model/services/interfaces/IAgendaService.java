package py.jere.agendate.model.services.interfaces;

import java.time.LocalDateTime;

import py.jere.agendate.model.entities.Agenda;
import py.jere.agendate.model.services.ServiceCustom;

public interface IAgendaService extends ServiceCustom<Agenda> {
	boolean horarioDisponible(LocalDateTime inicio, LocalDateTime fin);
}
