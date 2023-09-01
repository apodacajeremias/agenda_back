package py.jere.agendate.controller.utils.correos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class Correo {

	@Autowired
	private JavaMailSender mailSender;

	private Pattern pattern;

	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

	/**
	 * Se usa para enviar el correo, recibe un asunto y un correo destinatario
	 * 
	 * @param asunto
	 * @param destinatario
	 * @throws Exception
	 */
	void enviar(final String asunto, final String destinatario, final String mensaje) throws Exception {
		if (destinatario == null) {
			throw new CorreoNoEnviadoException("EL CORREO DESTINATARIO ES VACIO");
		} else if (!isValid(destinatario)) {
			throw new CorreoNoEnviadoException("EL CORREO DESTINATARIO NO TIENE EL FORMATO CORRECTO");
		} else {
			// Si el destinatario no esta vacio y es valido
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(destinatario);
			email.setSubject(asunto);
			email.setText(mensaje);
			mailSender.send(email);
		}
	}

	/**
	 * Se recibe un parametro de tipo ResponseEntity<Resource> porque al construir
	 * el documento PDF con la libreria reporte, dentro de las cabeceras HTTP del
	 * ResponseEntity consta el nombre del documento construido.
	 * 
	 * @param asunto
	 * @param destinatario
	 * @param mensaje
	 * @param adjunto
	 * @throws Exception
	 */
	void enviar(final String asunto, final String destinatario, final String mensaje,
			final ResponseEntity<Resource> adjunto) throws Exception {
		if (destinatario == null) {
			throw new CorreoNoEnviadoException("EL CORREO DESTINATARIO ES VACIO");
		} else if (!isValid(destinatario)) {
			throw new CorreoNoEnviadoException("EL CORREO DESTINATARIO NO TIENE EL FORMATO CORRECTO");
		} else {
			// Si el destinatario no esta vacio y es valido
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true);
			email.setTo(destinatario);
			email.setSubject(asunto);
			email.setText(mensaje);
			email.addAttachment(adjunto.getHeaders().getContentDisposition().getFilename(), adjunto.getBody());
			mailSender.send(email.getMimeMessage());
		}
	}

	/**
	 * Metodo sirve para validar si un correo tiene el formato correcto.
	 * 
	 * @param correo
	 * @return
	 */
	boolean isValid(final String correo) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(correo);
		return matcher.matches();
	}

}
