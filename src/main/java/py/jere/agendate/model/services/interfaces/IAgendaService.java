package py.jere.agendate.model.services.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import py.jere.agendate.model.entities.Agenda;
import py.jere.agendate.model.services.ServiceCustom;

public interface IAgendaService extends ServiceCustom<Agenda> {
	boolean horarioDisponible(LocalDateTime inicio, LocalDateTime fin);
	List<Agenda> buscarPorColaborador(UUID idColaborador);
	List<Agenda> buscarPorPersona(UUID idPersona);
}
