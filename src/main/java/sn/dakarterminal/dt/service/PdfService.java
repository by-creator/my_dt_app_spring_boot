package sn.dakarterminal.dt.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.dakarterminal.dt.entity.DossierFacturation;
import sn.dakarterminal.dt.entity.Facturation;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class PdfService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generateProforma(DossierFacturation dossier) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            addHeader(document, "PROFORMA");
            addDossierInfo(document, dossier);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating proforma PDF", e);
            throw new RuntimeException("Failed to generate proforma PDF", e);
        }
    }

    public byte[] generateFacture(Facturation facturation) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            addHeader(document, "FACTURE");
            addFactureInfo(document, facturation);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating facture PDF", e);
            throw new RuntimeException("Failed to generate facture PDF", e);
        }
    }

    private void addHeader(Document document, String title) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);

        Paragraph header = new Paragraph("DAKAR TERMINAL", titleFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph subtitle = new Paragraph("Port de Dakar - Sénégal", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        document.add(Chunk.NEWLINE);

        Font titleDocFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.BLACK);
        Paragraph titlePara = new Paragraph(title, titleDocFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);

        document.add(Chunk.NEWLINE);

        Paragraph datePara = new Paragraph("Date: " + LocalDateTime.now().format(FORMATTER),
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        datePara.setAlignment(Element.ALIGN_RIGHT);
        document.add(datePara);

        document.add(Chunk.NEWLINE);
    }

    private void addDossierInfo(Document document, DossierFacturation dossier) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        addTableCell(table, "Dossier ID:", String.valueOf(dossier.getId()));
        addTableCell(table, "Statut:", dossier.getStatut() != null ? dossier.getStatut().name() : "");
        if (dossier.getRattachementBl() != null) {
            addTableCell(table, "N° BL:", dossier.getRattachementBl().getBl());
            addTableCell(table, "Client:", dossier.getRattachementBl().getNom() + " " +
                    (dossier.getRattachementBl().getPrenom() != null ? dossier.getRattachementBl().getPrenom() : ""));
        }
        if (dossier.getDateProforma() != null) {
            addTableCell(table, "Date Proforma:", dossier.getDateProforma().format(FORMATTER));
        }

        document.add(table);
    }

    private void addFactureInfo(Document document, Facturation facturation) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        addTableCell(table, "N° Facture:", facturation.getNumeroFacture() != null ? facturation.getNumeroFacture() : "");
        addTableCell(table, "N° BL:", facturation.getNumeroBl() != null ? facturation.getNumeroBl() : "");
        addTableCell(table, "Client:", facturation.getClient() != null ? facturation.getClient() : "");
        addTableCell(table, "Navire:", facturation.getNavire() != null ? facturation.getNavire() : "");
        addTableCell(table, "Montant HT:", facturation.getMontantHt() != null ? facturation.getMontantHt().toString() + " XOF" : "");
        addTableCell(table, "TVA:", facturation.getMontantTva() != null ? facturation.getMontantTva().toString() + " XOF" : "");
        addTableCell(table, "Montant TTC:", facturation.getMontantTtc() != null ? facturation.getMontantTtc().toString() + " XOF" : "");

        document.add(table);
    }

    private void addTableCell(PdfPTable table, String label, String value) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }
}
