package py.jere.agendate.controller.utils.reportes;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * Libreria para generar documentos con JasperReport, permite previsualizar
 * dentro del navegador o descargar el documento en formato PDF. Se reestructura
 * la clase considerando las modificaciones de los reportes solicitados por la
 * direccion.
 * 
 * @author Jeremias Apodaca
 * @version 2
 *
 */

@Component
public class Reporte {

//	@Autowired
//	private GlobalControllerAdvice advice;
//
//	@Autowired
//	private ManageFiles manageFiles;

	final private MediaType mediaType = MediaType.APPLICATION_PDF;

	final private String PLANTILLA_FILENAME = "PlantillaOficial.jrxml";

	final public String FORMATO_DECIMAL = "#,#00.00#;#,#00.00#-";

	/**
	 * Indica al navegador que el documento sera descargado en formato PDF.
	 * 
	 * @param filename
	 * @param params
	 * @param data
	 * @return
	 */
	protected ResponseEntity<Resource> generar(String filename, Map<String, Object> params, Collection<?> data,
			BuilderType type) {
		return switch (type) {
		case DESCARGAR: {
			yield descargar(filename, params, data);
		}
		case PREVISUALIZAR: {
			yield previsualizar(filename, params, data);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		};
	}

	/**
	 * Indica al navegador que el documento sera descargado en formato PDF.
	 * 
	 * @param filename
	 * @param params
	 * @param data
	 * @return
	 */
	protected ResponseEntity<Resource> descargar(String filename, Map<String, Object> params, Collection<?> data) {
		try {
			byte[] reporte = JasperExportManager.exportReportToPdf(generarDocumento(filename, params, data));
			return ResponseEntity.ok().contentLength((long) reporte.length)
					.headers(httpHeaders(params, BuilderType.DESCARGAR)).body(new ByteArrayResource(reporte));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Indica al navegador que el documento sera previsualizado en una pestaña.
	 * 
	 * @param filename
	 * @param params
	 * @param data
	 * @return
	 */
	public ResponseEntity<Resource> previsualizar(String filename, Map<String, Object> params, Collection<?> data) {
		try {
			byte[] reporte = JasperExportManager.exportReportToPdf(generarDocumento(filename, params, data));
			return ResponseEntity.ok().contentLength((long) reporte.length)
					.headers(httpHeaders(params, BuilderType.PREVISUALIZAR)).body(new ByteArrayResource(reporte));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Compilar el documento en crudo, sin formato, sin ajustes, sin parametros,
	 * solamente un documento vacio. Se usa para compilar subreportes
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	public JasperReport compilar(String filename) throws FileNotFoundException, JRException {
		JasperDesign design = documento(filename);
		JasperReport report = JasperCompileManager.compileReport(design);
		return report;
	}

	/**
	 * Reune los parametros, la Collectiona de datos en un documento compilado.
	 * 
	 * @param filename
	 * @param params
	 * @return
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	private JasperPrint generarDocumento(String filename, Map<String, Object> params, Collection<?> data)
			throws FileNotFoundException, JRException {
		// Se agregan todos los parametros necesarios para construir: titulo, encabezado
		// y pie de pagina
		construirElementosPadronizados(params);
		// Caso se decida ignorar el formato de la plantilla, buscamos el valor de
		// IGNORAR_FORMATO en el Map
		if (params.containsValue(DocumentType.IGNORAR_FORMATO)) {
			JasperDesign design = documento(filename);
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(data));
			return print;
		} else {
			// Caso se decida seguir el formato de la plantilla
			JasperDesign design = fundir(filename, params);
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(data));
			return print;
		}

	}

	/**
	 * Se busca la plantilla para los demás documentos, el intuito de esta plantilla
	 * es estandarizar el formato de titulo de pagina (nombre de la universidad,
	 * nombre de la facultad, logos), igualar el formato de {TITULO} y {SUBTITULO}
	 * en encabezado de pagina: esta banda contiene la numeracion de pagina a pedido
	 * de la direccion, se estandariza tambien el pie de pagina agregando
	 * informacion de Mision en castellano y guarani, como esta informacion puede
	 * variar de tamano el texto es responsivo, ademas se agrega en esta banda la
	 * informacion de la institucion. Cada una de las bandas tiene 50px de alto,
	 * ocupando 150px de la pagina en total.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	private JasperDesign plantilla() throws FileNotFoundException, JRException {
		return JRXmlLoader.load(ResourceUtils.getFile("classpath:jasper/style/" + PLANTILLA_FILENAME));
	}

	/**
	 * Se busca el documento a emitir. Es importante recordar que todas las bandas
	 * se pueden usar en el documento, pero las siguientes bandas seran
	 * sobreescritas segun la plantilla: Title, PageHeader, PageFooter no se deben
	 * usar.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	private JasperDesign documento(String filename) throws FileNotFoundException, JRException {
		return JRXmlLoader.load(ResourceUtils.getFile("classpath:jasper/docs/" + filename));
	}

	/**
	 * La plantilla sobreescribe el titulo, el encabezado de pagina, y el pie de
	 * pagina segun el formato de la plantilla en vigencia Es importante destacar
	 * que existen parametros que determinan la inclusion del titulo, encabeza y pie
	 * de pagina. {}
	 * 
	 * @param plantilla
	 * @param reporte
	 * @return
	 * @throws JRException
	 * @throws FileNotFoundException
	 */
	private JasperDesign fundir(String filename, Map<String, Object> params) throws FileNotFoundException, JRException {
		JasperDesign plantilla = plantilla();
		JasperDesign documento = documento(filename);
		// Verifica si hay que ignorar alguna parte de la plantilla
		if (!params.containsValue(DocumentType.IGNORAR_TITULO))
			documento.setTitle(plantilla.getTitle());

		if (!params.containsValue(DocumentType.IGNORAR_ENCABEZADO))
			documento.setPageHeader(plantilla.getPageHeader());

		if (!params.containsValue(DocumentType.IGNORAR_PIE))
			documento.setPageFooter(plantilla.getPageFooter());

		plantilla.getParametersList().forEach(param -> {
			try {
				documento.addParameter(param);
			} catch (JRException e) {
				// NADA
			}
		});

		return documento;
	}

	/**
	 * Se asignan headers para que el navegador entienda qué se quiere hacer.
	 * 
	 * @param filename
	 * @param type
	 * @return
	 */
	private HttpHeaders httpHeaders(Map<String, Object> params, BuilderType type) {
		String newFilename = construirNombreDeDocumento(params);
		ContentDisposition contentDisposition = ContentDisposition.builder(type.getType()).filename(newFilename)
				.build();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(mediaType);
		header.setContentDisposition(contentDisposition);
		return header;
	}

	/**
	 * Se agregan parámetros que son obligatorios la construccion del titulo,
	 * encabezado y pie de pagina
	 * 
	 * @param params
	 */
	private void construirElementosPadronizados(Map<String, Object> params) {
//		final UsuarioEntity USUARIO = advice.getUsuario();
//		params.put("IMG1", manageFiles.buscarImagen(USUARIO.getFacultad().getUniversidad().getLogo()));
//		params.put("IMG2", manageFiles.buscarImagen(USUARIO.getFacultad().getLogo()));
//		params.put("FACULTAD", USUARIO.getFacultad());
//		params.put("IMPRESO_POR", USUARIO.getPersona().getNombreCompleto());

		// Verificar si tiene el parametro TITULO
		if (!params.containsKey("TITULO")) {
			params.put("TITULO", "DOCUMENTO SIN TITULO");
		}

		// Verificar si tiene el parametro SUBTITULO
		if (!params.containsKey("SUBTITULO")) {
			params.put("SUBTITULO", null);
		}
	}

	private String construirNombreDeDocumento(Map<String, Object> params) {
		String nombreDocumento = "";
		final Object TITULO = params.get("TITULO");
		final Object SUBTITULO = params.get("SUBTITULO");
		if (params.containsKey("TITULO") && TITULO != null) {
			nombreDocumento = nombreDocumento.concat(TITULO.toString().replace("\s", "_"));
		}
		// Verificar si tiene el parametro SUBTITULO
		if (params.containsKey("SUBTITULO") && SUBTITULO != null) {
			nombreDocumento = nombreDocumento.concat("-" + SUBTITULO.toString().replace("\s", "_"));
		}

		if ((!params.containsKey("TITULO") || TITULO == null)
				&& (!params.containsKey("SUBTITULO") || SUBTITULO == null)) {
			nombreDocumento = "DOCUMENTO_SIN_NOMBRE";
		}

//		nombreDocumento = nombreDocumento.concat("-" + manageFiles.randomAlphaNumeric(10)).concat(".pdf");
		return nombreDocumento;
	}
}
