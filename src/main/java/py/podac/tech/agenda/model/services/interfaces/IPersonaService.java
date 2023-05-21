package py.podac.tech.agenda.model.services.interfaces;

import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.model.services.ServiceCustom;

public interface IPersonaService extends ServiceCustom<Persona> {
	Persona buscarPorEmailDeUsuario(String email);
	
}
