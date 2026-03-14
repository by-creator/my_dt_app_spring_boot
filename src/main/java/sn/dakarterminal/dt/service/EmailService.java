package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@dakarterminal.sn}")
    private String fromEmail;

    @Value("${app.mail.from-name:Dakar Terminal}")
    private String fromName;

    @Async
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(message);
            log.info("Email sent to {} with subject: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("HTML email sent to {} with subject: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Async
    public void sendRattachementNotification(String to, String bl, String statut) {
        String subject = "[Dakar Terminal] Mise à jour de votre rattachement BL " + bl;
        String body = String.format(
            "Bonjour,\n\nVotre demande de rattachement pour le BL %s a été mise à jour.\nStatut: %s\n\nCordialement,\nDakar Terminal",
            bl, statut
        );
        sendSimpleEmail(to, subject, body);
    }

    @Async
    public void sendDossierNotification(String to, String numeroBl, String statut, String message) {
        String subject = "[Dakar Terminal] Dossier de facturation - BL " + numeroBl;
        String body = String.format(
            "Bonjour,\n\nVotre dossier de facturation pour le BL %s a été mis à jour.\nStatut: %s\n%s\n\nCordialement,\nDakar Terminal",
            numeroBl, statut, message != null ? message : ""
        );
        sendSimpleEmail(to, subject, body);
    }

    @Async
    public void sendTicketNotification(String to, String ticketNumero, String guichet) {
        String subject = "[Dakar Terminal] Votre ticket " + ticketNumero + " est appelé";
        String body = String.format(
            "Bonjour,\n\nVotre ticket numéro %s est maintenant appelé au guichet %s.\nMerci de vous présenter.\n\nCordialement,\nDakar Terminal",
            ticketNumero, guichet
        );
        sendSimpleEmail(to, subject, body);
    }
}
