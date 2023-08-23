package py.jere.agendate.model.services.interfaces;

import java.util.UUID;

import py.jere.agendate.model.entities.Usuario;
import py.jere.agendate.model.services.ServiceCustom;
import py.jere.agendate.security.auth.VerificationToken;

public interface IUsuarioService extends ServiceCustom<Usuario> {

	Usuario buscarPorEmail(String email);

	boolean existeEmail(String email);

	Usuario getUsuario(String verificationToken);

	void saveRegisteredUsuario(Usuario usuario);

	void createVerificationToken(Usuario usuario, String token);

	VerificationToken getVerificationToken(String verificationToken);

	void createPasswordResetTokenForUsuario(Usuario usuario, String token);

	String validatePasswordResetToken(String token);

	Usuario getUsuarioByPasswordResetToken(String token);

	void changeUsuarioPassword(Usuario usuario, String password) throws Exception;

	VerificationToken generateNewVerificationToken(final String existingVerificationToken);

	boolean checkIfValidOldPassword(final Usuario usuario, final String oldPassword);

	void activarCuenta(UUID ID) throws Exception;

}
