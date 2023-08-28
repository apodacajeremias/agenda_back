package py.jere.agendate.controller.utils.correo;

//@Component
//public class NotificacionAplazoListener implements ApplicationListener<OnNotificacionAplazoEvent> {
//	@Autowired
//	private INotificacionAplazoService service;
//	@Autowired
//	private NotificacionAplazoController controller;
//	@Autowired
//	private Correo correo;
//	@Override
//	public void onApplicationEvent(OnNotificacionAplazoEvent event) {
//		this.confirmarEnvioDeNotificacion(event);
//	}
//	@Transactional
//	private void confirmarEnvioDeNotificacion(OnNotificacionAplazoEvent event) {
//		final NotificacionAplazoEntity NOTIFICACION = event.getNotificacionAplazo();
//		String recipientAddress = NOTIFICACION.getPersona().getEmail();
//		String subject = "NOTIFICACION DE APLAZO " + NOTIFICACION.getCarrera().getDescripcion().toUpperCase();
//		String message = "Se remite notificacion de aplazo de la "
//				+ NOTIFICACION.getCarrera().getFilial().getFacultad().getAbreviatura();
//		SimpleMailMessage email = new SimpleMailMessage();
//		email.setTo(recipientAddress);
//		email.setSubject(subject);
//		email.setText(message);
//		System.out.println("CORREO NOTIFICACION DE APLAZO " + NOTIFICACION.getId());
//		// SE ENVIA EL CORREO
////		mailSender.send(email); // NO ENVIAR
//		// SI NO ARROJA ERROR, SE ACTUALIZA EL REGISTRO
//		service.confirmarEnvioCorreo(NOTIFICACION.getId());
//	}
//	@Transactional
//	private void confirmarEnvioDeNotificacion(OnNotificacionAplazoEvent event) {
//		final NotificacionAplazoEntity NOTIFICACION = event.getNotificacionAplazo();
//		String asunto = "NOTIFICACION DE APLAZO: " + NOTIFICACION.getPersona().getNombreCompleto();
//		String destinatario = NOTIFICACION.getPersona().getEmail();
//		String mensaje = "Se remite notificacion de aplazo de la "
//				+ NOTIFICACION.getCarrera().getFilial().getFacultad().getDescripcion();
//		ResponseEntity<Resource> adjunto = controller.imprimir(NOTIFICACION.getId());
//		try {
//			correo.enviar(asunto, destinatario, mensaje, adjunto);
//			// SI NO ARROJA ERROR, SE ACTUALIZA EL REGISTRO
//			service.confirmarEnvioCorreo(NOTIFICACION.getId());
//			NOTIFICACION.setCorreoEnviado(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			NOTIFICACION.setCorreoEnviado(false);
//		}
//	}
//}