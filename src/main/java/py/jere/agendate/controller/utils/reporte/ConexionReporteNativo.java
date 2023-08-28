package py.jere.agendate.controller.utils.reporte;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.util.ResourceUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class ConexionReporteNativo {

	protected static Connection con;

	@PersistenceContext
	private EntityManager entityManager;

	public void descargarReporteExcel(Map<String, Object> parametros, HttpServletResponse response, String nombreReporte, String nombreArchivo) throws JRException, IOException {

		Session session = (Session) entityManager.getDelegate();
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				ConexionReporteNativo.con = connection;
			}
		});

		JasperReport reporte = JasperCompileManager.compileReport(ResourceUtils.getFile("classpath:jasper/docs/" + nombreReporte + ".jrxml").getAbsolutePath());
		JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, ConexionReporteNativo.con);
		JRXlsxExporter exporter = new JRXlsxExporter();
		SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
		reportConfigXLS.setSheetNames(new String[] { "sheet1" });
		reportConfigXLS.setWrapText(true);
		reportConfigXLS.setDetectCellType(true);
		reportConfigXLS.setRemoveEmptySpaceBetweenRows(true);
		reportConfigXLS.setRemoveEmptySpaceBetweenColumns(true);
		exporter.setConfiguration(reportConfigXLS);
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		response.setHeader("Content-Disposition", "attachment;filename=" + nombreArchivo + "_" + LocalDateTime.now() + ".xlsx");
		response.setContentType("application/octet-stream");
		exporter.exportReport();
	}

}
