package py.podac.tech.agenda.model.services.interfaces;

import py.podac.tech.agenda.model.entities.Colaborador;
import py.podac.tech.agenda.model.services.ServiceCustom;

public interface IColaboradorService extends ServiceCustom<Colaborador> {
	boolean existeRegistroContribuyente(String registroContribuyente);
	boolean existeRegistroProfesional(String registroProfesional);
	
}
