package sn.dakarterminal.dt.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sn.dakarterminal.dt.service.AuditExportService;
import sn.dakarterminal.dt.service.AuditService;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/audit")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
public class AuditWebController {

    private final AuditService auditService;
    private final AuditExportService auditExportService;

    @GetMapping
    public String auditLogs(
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var logs = auditService.search(userEmail, action, entityType, dateFrom, dateTo, pageable);

        model.addAttribute("logs", logs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logs.getTotalPages());
        model.addAttribute("filterUser", userEmail);
        model.addAttribute("filterAction", action);
        model.addAttribute("filterEntity", entityType);
        model.addAttribute("filterDateFrom", dateFrom);
        model.addAttribute("filterDateTo", dateTo);
        model.addAttribute("pageTitle", "Journaux d'Audit");
        model.addAttribute("actions", new String[]{"LOGIN", "LOGOUT", "CREATE", "UPDATE", "DELETE", "DEACTIVATE", "TOGGLE"});
        model.addAttribute("entities", new String[]{"USER", "ROLE", "AUTH"});
        return "admin/audit/logs";
    }

    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTo,
            @RequestParam(defaultValue = "xlsx") String format,
            HttpServletResponse response) throws IOException {

        var logs = auditService.searchAll(userEmail, action, entityType, dateFrom, dateTo);

        if ("pdf".equalsIgnoreCase(format)) {
            byte[] pdf = auditExportService.exportPdf(logs);
            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audit_logs.pdf\"");
            response.getOutputStream().write(pdf);
        } else {
            byte[] xlsx = auditExportService.exportXlsx(logs);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audit_logs.xlsx\"");
            response.getOutputStream().write(xlsx);
        }
    }
}
