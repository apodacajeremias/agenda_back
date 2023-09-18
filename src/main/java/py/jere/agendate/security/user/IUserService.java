package py.jere.agendate.security.user;

import java.util.UUID;

import py.jere.agendate.model.services.ServiceCustom;

public interface IUserService extends ServiceCustom<User> {
	User buscarPorEmail(String email);
	boolean existeEmail(String email);
	void activarCuenta(UUID id) throws Exception;
	void activarCambioContrasena(UUID id);
	void cambiarContrasena(User user, String newPassword) throws Exception;
}
