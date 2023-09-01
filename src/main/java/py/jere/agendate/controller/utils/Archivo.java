package py.jere.agendate.controller.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Archivo {

//	@Autowired
	private String carpeta;

	// Guardar archivo
	public String guardarArchivo(MultipartFile multiPart) {
		try {
			return guardar(multiPart, carpeta);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String guardarArchivo(MultipartFile multiPart, String nuevoNombreArchivo) {
		try {
			return guardar(multiPart, carpeta, nuevoNombreArchivo);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Guardar imagenes
	public String guardarImagen(MultipartFile multiPart) {
		try {
			return guardar(multiPart, carpeta);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String guardarImagen(MultipartFile multiPart, String nuevoNombreArchivo) {
		try {
			return guardar(multiPart, carpeta, nuevoNombreArchivo);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Guardar
	private String guardar(MultipartFile multiPart, String rutaDirectorio) throws IllegalStateException, IOException {
		if (!existe(rutaDirectorio)) {
			crear(rutaDirectorio);
		}
		File imageFile = new File(
				rutaDirectorio + randomAlphaNumeric(25) + '.' + extension(multiPart.getOriginalFilename()));
		multiPart.transferTo(imageFile);
		return imageFile.getName();
	}

	private String guardar(MultipartFile multiPart, String rutaDirectorio, String nuevoNombreArchivo)
			throws IllegalStateException, IOException {
		if (!existe(rutaDirectorio)) {
			crear(rutaDirectorio);
		}
		File imageFile = new File(
				rutaDirectorio + nuevoNombreArchivo + '.' + extension(multiPart.getOriginalFilename()));
		multiPart.transferTo(imageFile);
		return imageFile.getName();
	}

	// Buscar archivo
	public File buscarArchivo(String nombreArchivo) {
		try {
			return buscar(carpeta, nombreArchivo);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Buscar imagen
	public File buscarImagen(String nombreArchivo) {
		try {
			return buscar(carpeta + nombreArchivo);
		} catch (IllegalStateException | IOException e) {
			return null;
		}
	}

	// Buscar
	private File buscar(String rutaArchivo) throws IllegalStateException, IOException {
		return new File(rutaArchivo);
	}

	private File buscar(String rutaDirectorio, String nombreArchivo) throws IllegalStateException, IOException {
		return new File(rutaDirectorio + nombreArchivo);
	}

	// Elimina archivo donde indica rutaArchivo
	public boolean eliminar(String rutaArchivo) throws IOException {
		return Files.deleteIfExists(Paths.get(rutaArchivo));
	}

	// Verificar si existe directorio
	private boolean existe(String rutaDirectorio) {
		return new File(rutaDirectorio).exists();
	}

	// Crear directorio
	private void crear(String rutaDirectorio) throws IOException {
		Files.createDirectories(Paths.get(rutaDirectorio));
	}

	// Obtiene extension de archivo
	private String extension(String nombreArchivo) {
		String extension = "";
		int i = nombreArchivo.lastIndexOf('.');
		if (i > 0) {
			extension = nombreArchivo.substring(i + 1);
		}
		return extension;
	}

	// Asignar nombre aleatorio
	public String randomAlphaNumeric(int count) {
		String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length());
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}

}
