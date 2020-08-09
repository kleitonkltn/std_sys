
package dev.krtechs.services;

import static net.sf.jasperreports.engine.JasperFillManager.fillReport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.jsf.FacesContextUtils;
import org.springframework.web.server.ResponseStatusException;

import dev.krtechs.model.Estabelecimento;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class JasperReportsService {
    FacesContextUtils facesContext;

    public byte[] generatePDFReport(final List<Estabelecimento> listaUs) {
        byte[] bytes = null;
        Map<String, Object> myMap = new HashMap<String, Object>();
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
            JasperPrint printReport;
            JasperReport pathjrxml;
            String pathFile = new File(".").getAbsolutePath().replace(".", "reportFiles\\report.jrxml");
            pathjrxml = JasperCompileManager.compileReport(pathFile);
            printReport = fillReport(pathjrxml, myMap, new JRBeanCollectionDataSource(listaUs));
            bytes = JasperExportManager.exportReportToPdf(printReport);
        } catch (JRException | IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Error to generate PDF file, contact to administrator of system");
        }
        return bytes;
    }
    
}