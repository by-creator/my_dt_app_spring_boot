package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DematEmailService {

    private final JavaMailSender mailSender;

    private static final String[] RECIPIENTS = {
            "noreplysitedt@gmail.com",
            "iosid242@gmail.com"
    };

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ── VALIDATION ────────────────────────────────────────────

    @Async
    public void sendValidationEmail(String nom, String prenom, String email,
                                    String numeroBl, String maisonTransit,
                                    MultipartFile fileBl,
                                    MultipartFile fileBadShipping,
                                    MultipartFile fileDeclaration) {

        Map<String, String> fichiers = new LinkedHashMap<>();
        fichiers.put("BL",           fileInfo(fileBl));
        fichiers.put("BAD SHIPPING", fileInfo(fileBadShipping));
        fichiers.put("DECLARATION",  fileInfo(fileDeclaration));

        List<MultipartFile> attachments = new ArrayList<>();
        addIfPresent(attachments, fileBl);
        addIfPresent(attachments, fileBadShipping);
        addIfPresent(attachments, fileDeclaration);

        String subject = "[Dakar Terminal] Nouvelle demande de validation — " + nom + " " + prenom;
        String html    = buildHtml("Demande de validation", "#1565C0",
                nom, prenom, email, numeroBl, maisonTransit, fichiers);
        sendToAll(subject, html, attachments);
    }

    // ── REMISE ────────────────────────────────────────────────

    @Async
    public void sendRemiseEmail(String nom, String prenom, String email,
                                String numeroBl, String maisonTransit,
                                MultipartFile fileDemandeManuscrite,
                                MultipartFile fileBadShipping,
                                MultipartFile fileBl,
                                MultipartFile fileFacture,
                                MultipartFile fileDeclaration) {

        Map<String, String> fichiers = new LinkedHashMap<>();
        fichiers.put("DEMANDE MANUSCRITE", fileInfo(fileDemandeManuscrite));
        fichiers.put("BAD SHIPPING",       fileInfo(fileBadShipping));
        fichiers.put("BL",                 fileInfo(fileBl));
        fichiers.put("FACTURE",            fileInfo(fileFacture));
        fichiers.put("DECLARATION",        fileInfo(fileDeclaration));

        List<MultipartFile> attachments = new ArrayList<>();
        addIfPresent(attachments, fileDemandeManuscrite);
        addIfPresent(attachments, fileBadShipping);
        addIfPresent(attachments, fileBl);
        addIfPresent(attachments, fileFacture);
        addIfPresent(attachments, fileDeclaration);

        String subject = "[Dakar Terminal] Nouvelle demande de remise — " + nom + " " + prenom;
        String html    = buildHtml("Demande de remise", "#4B49AC",
                nom, prenom, email, numeroBl, maisonTransit, fichiers);
        sendToAll(subject, html, attachments);
    }

    // ── Internal ──────────────────────────────────────────────

    private void sendToAll(String subject, String html, List<MultipartFile> attachments) {
        ClassPathResource logo = new ClassPathResource("static/img/image.png");

        for (String to : RECIPIENTS) {
            try {
                MimeMessage msg = mailSender.createMimeMessage();
                // multipart=true to support inline + attachments
                MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
                helper.setFrom("noreply@dakarterminal.sn", "Dakar Terminal");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(html, true);

                // Inline logo
                if (logo.exists()) {
                    helper.addInline("logo", logo);
                }

                // File attachments
                for (MultipartFile file : attachments) {
                    String filename = file.getOriginalFilename();
                    if (filename == null || filename.isBlank()) filename = "fichier";
                    helper.addAttachment(filename, file.getResource());
                }

                mailSender.send(msg);
                log.info("Demat email sent to {}", to);
            } catch (Exception e) {
                log.error("Failed to send demat email to {}: {}", to, e.getMessage());
            }
        }
    }

    private String buildHtml(String type, String accentColor,
                              String nom, String prenom, String email,
                              String numeroBl, String maisonTransit,
                              Map<String, String> fichiers) {

        String date = LocalDateTime.now().format(DATE_FMT);

        StringBuilder fichierRows = new StringBuilder();
        if (fichiers != null) {
            fichiers.forEach((label, val) -> {
                if (val != null && !val.isEmpty()) {
                    fichierRows.append(row(label,
                            "<span style='color:#28a745'>&#10003; Fourni (" + val + ")</span>"));
                } else {
                    fichierRows.append(row(label,
                            "<span style='color:#dc3545'>Non fourni</span>"));
                }
            });
        }

        return "<!DOCTYPE html>" +
                "<html lang='fr'><head><meta charset='UTF-8'/></head><body style='margin:0;padding:0;" +
                "background:#f4f7ff;font-family:Helvetica,Arial,sans-serif;'>" +

                "<table width='100%' cellpadding='0' cellspacing='0' style='background:#f4f7ff;padding:40px 0'>" +
                "<tr><td align='center'>" +
                "<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff;" +
                "border-radius:12px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,.08);'>" +

                // Header with logo
                "<tr><td style='background:#ffffff;padding:24px 32px;text-align:center;" +
                "border-bottom:3px solid " + accentColor + "'>" +
                "<img src='cid:logo' alt='Dakar Terminal' style='max-width:260px;max-height:46px;" +
                "width:auto;height:auto;display:block;margin:0 auto'/>" +
                "<p style='color:" + accentColor + ";margin:10px 0 0;font-size:14px;font-weight:700;" +
                "letter-spacing:.5px'>" + type.toUpperCase() + "</p>" +
                "</td></tr>" +

                // Body
                "<tr><td style='padding:32px'>" +
                "<p style='color:#555;font-size:13px;margin:0 0 20px'>Reçue le <strong>" + date + "</strong></p>" +
                "<table width='100%' cellpadding='0' cellspacing='0' style='border-collapse:collapse'>" +
                row("Nom", nom) +
                row("Prénom", prenom) +
                row("Email", email) +
                row("Numéro de BL", numeroBl != null && !numeroBl.isEmpty() ? numeroBl : "—") +
                row("Maison de transit", maisonTransit != null && !maisonTransit.isEmpty() ? maisonTransit : "—") +
                fichierRows +
                "</table></td></tr>" +

                // Footer
                "<tr><td style='background:#f8f9ff;padding:18px 32px;text-align:center;" +
                "border-top:1px solid #e8e8f0'>" +
                "<p style='color:#aaa;font-size:11px;margin:0'>" +
                "&copy; 2026 DakarTerminal — Ce message est généré automatiquement.</p>" +
                "</td></tr>" +

                "</table></td></tr></table>" +
                "</body></html>";
    }

    private static String row(String label, String value) {
        return "<tr style='border-bottom:1px solid #f0f0f0'>" +
                "<td style='padding:10px 0;font-size:13px;font-weight:700;color:#444;" +
                "width:180px;vertical-align:top'>" + label + "</td>" +
                "<td style='padding:10px 0;font-size:13px;color:#333;vertical-align:top'>" +
                (value != null ? value : "—") + "</td>" +
                "</tr>";
    }

    private static String fileInfo(MultipartFile f) {
        if (f == null || f.isEmpty()) return null;
        String name = f.getOriginalFilename();
        long kb = f.getSize() / 1024;
        return (name != null ? name : "fichier") + " (" + kb + " Ko)";
    }

    private static void addIfPresent(List<MultipartFile> list, MultipartFile f) {
        if (f != null && !f.isEmpty()) list.add(f);
    }
}
