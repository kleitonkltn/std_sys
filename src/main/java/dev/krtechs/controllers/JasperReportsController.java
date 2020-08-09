package dev.krtechs.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.krtechs.core.exception.DateFormatterException;
import dev.krtechs.model.Estabelecimento;
import dev.krtechs.repository.EstabelecimentoRepository;
import dev.krtechs.services.JasperReportsService;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unchecked")
public class JasperReportsController {
    final private EstabelecimentoRepository repository;
    final private JasperReportsService jasperReportsService;

    @Autowired
    public JasperReportsController(EstabelecimentoRepository repository, JasperReportsService jasperService) {
        this.repository = repository;
        jasperReportsService = jasperService;
    }

    @RequestMapping(value = "report/estabelecimentos/all", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public ResponseEntity<?> reportAll() {
        List<Estabelecimento> listEstabelecimentos = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        byte[] bytes = jasperReportsService.generatePDFReport(listEstabelecimentos);
        return ResponseEntity.ok().header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + ".pdf\"").body(bytes);
    }

    /*
     * * the client must sends an json type content with the variables startDate and
     * endDate the type String and format dd/MM/yyyy
     */
    @RequestMapping(value = "report/estabelecimentos/date", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public ResponseEntity<?> reportByDate(@RequestBody Map<String, Object> jsonData) {
        verifyParamms(jsonData, new String[] { "startDate", "endDate" });
        try {
            LocalDate startDate = LocalDate.parse(jsonData.get("startDate").toString(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate endDate = LocalDate.parse(jsonData.get("endDate").toString(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            List<Estabelecimento> listEstabelecimentos = repository.findByCreatedAtEstabelecimentos(startDate, endDate);
            byte[] bytes = jasperReportsService.generatePDFReport(listEstabelecimentos);
            return ResponseEntity.ok().header("Content-Type", "application/pdf; charset=UTF-8")
                    .header("Content-Disposition", "inline; filename=\"" + ".pdf\"").body(bytes);
        } catch (DateTimeParseException e) {
            throw new DateFormatterException("dd/MM/yyyy");
        }
    }

    @RequestMapping(value = "/report/estabelecimentos/multipleids", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public ResponseEntity<?> reportByIds(@RequestBody Map<String, Object> jsonData) {
        verifyParamms(jsonData, new String[] { "ids" });
        List<Integer> listIdsEstabelecimentos = (List<Integer>) jsonData.get("ids");
        List<Estabelecimento> listEstabelecimentos = repository.findByIDsEstabelecimentos(listIdsEstabelecimentos);
        byte[] bytes = jasperReportsService.generatePDFReport(listEstabelecimentos);
        return ResponseEntity.ok().header("Content-Type", "application/pdf; charset=UTF-8")
                .header("Content-Disposition", "inline; filename=\"" + ".pdf\"").body(bytes);
    }

    private void verifyParamms(Map<String, Object> jsonData, String[] parameters) {
        for (String parameter : parameters) {
            if (jsonData.get(parameter) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "The request must contain in body the fields: " + String.join(", ", parameters));
            }
        }
    }
}