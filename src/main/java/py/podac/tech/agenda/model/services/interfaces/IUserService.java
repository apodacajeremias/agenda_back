package py.podac.tech.agenda.model.services.interfaces;

import java.util.UUID;

import py.podac.tech.agenda.model.services.ServiceCustom;
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.VerificationToken;

public interface IUserService extends ServiceCustom<User> {

	User buscarPorEmail(String email);

	boolean existeEmail(String email);

	User getUser(String verificationToken);

	void saveRegisteredUser(User user);

	void createVerificationToken(User user, String token);

	VerificationToken getVerificationToken(String verificationToken);

	void createPasswordResetTokenForUser(User user, String token);

	String validatePasswordResetToken(String token);

	User getUserByPasswordResetToken(String token);

	void changeUserPassword(User user, String password) throws Exception;

	VerificationToken generateNewVerificationToken(final String existingVerificationToken);

	boolean checkIfValidOldPassword(final User user, final String oldPassword);

	void activarCuenta(UUID ID) throws Exception;

}
