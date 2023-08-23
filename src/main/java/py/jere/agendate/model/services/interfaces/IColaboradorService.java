package py.jere.agendate.model.services.interfaces;

import py.jere.agendate.model.entities.Colaborador;
import py.jere.agendate.model.services.ServiceCustom;

public interface IColaboradorService extends ServiceCustom<Colaborador> {
	boolean existeRegistroContribuyente(String registroContribuyente);
	boolean existeRegistroProfesional(String registroProfesional);
	
}
