package sn.dakarterminal.dt.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import sn.dakarterminal.dt.dto.AuditLogDto;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditExportService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String[] HEADERS = {"#", "Date / Heure", "Utilisateur", "Email", "Action", "Entité", "Détails", "IP", "Statut"};

    public byte[] exportXlsx(List<AuditLogDto> logs) {
        try (XSSFWorkbook wb = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = wb.createSheet("Audit Logs");

            // Purple header style
            XSSFCellStyle headerStyle = wb.createCellStyle();
            XSSFColor purple = new XSSFColor(new Color(75, 73, 172), null);
            headerStyle.setFillForegroundColor(purple);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            XSSFFont headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);

            // Alternating row style
            XSSFCellStyle altStyle = wb.createCellStyle();
            altStyle.setFillForegroundColor(new XSSFColor(new Color(248, 249, 250), null));
            altStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Header row
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 1;
            for (AuditLogDto log : logs) {
                XSSFRow row = sheet.createRow(rowNum);
                XSSFCellStyle rowStyle = (rowNum % 2 == 0) ? altStyle : null;

                createCell(row, 0, String.valueOf(rowNum), rowStyle);
                createCell(row, 1, log.getCreatedAt() != null ? FMT.format(log.getCreatedAt()) : "", rowStyle);
                createCell(row, 2, nvl(log.getUserName()), rowStyle);
                createCell(row, 3, nvl(log.getUserEmail()), rowStyle);
                createCell(row, 4, nvl(log.getAction()), rowStyle);
                createCell(row, 5, nvl(log.getEntityType()), rowStyle);
                createCell(row, 6, nvl(log.getDetails()), rowStyle);
                createCell(row, 7, nvl(log.getIpAddress()), rowStyle);
                createCell(row, 8, nvl(log.getStatus()), rowStyle);
                rowNum++;
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération Excel", e);
        }
    }

    public byte[] exportPdf(List<AuditLogDto> logs) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate(), 20, 20, 30, 30);
            PdfWriter.getInstance(doc, out);
            doc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            titleFont.setColor(75, 73, 172);
            Paragraph title = new Paragraph("Journaux d'Audit — Dakar Terminal", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(4);
            doc.add(title);

            Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
            Paragraph sub = new Paragraph("Exporté le " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    + "  ·  " + logs.size() + " entrée(s)", subFont);
            sub.setAlignment(Element.ALIGN_CENTER);
            sub.setSpacingAfter(14);
            doc.add(sub);

            // Table columns widths
            PdfPTable table = new PdfPTable(new float[]{1.5f, 3.5f, 2.5f, 3f, 2f, 1.5f, 5f, 2.5f, 1.5f});
            table.setWidthPercentage(100);
            table.setHeaderRows(1);

            // Header cells
            Color headerBg = new Color(75, 73, 172);
            Font hFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, Color.WHITE);
            for (String h : HEADERS) {
                PdfPCell cell = new PdfPCell(new Phrase(h, hFont));
                cell.setBackgroundColor(headerBg);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderColor(Color.WHITE);
                table.addCell(cell);
            }

            // Data cells
            Font dFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
            dFont.setColor(Color.DARK_GRAY);
            int idx = 1;
            for (AuditLogDto log : logs) {
                Color rowBg = (idx % 2 == 0) ? new Color(248, 249, 250) : Color.WHITE;
                addPdfCell(table, String.valueOf(idx++), dFont, rowBg, Element.ALIGN_CENTER);
                addPdfCell(table, log.getCreatedAt() != null ? FMT.format(log.getCreatedAt()) : "", dFont, rowBg, Element.ALIGN_LEFT);
                addPdfCell(table, nvl(log.getUserName()), dFont, rowBg, Element.ALIGN_LEFT);
                addPdfCell(table, nvl(log.getUserEmail()), dFont, rowBg, Element.ALIGN_LEFT);
                addPdfCell(table, nvl(log.getAction()), dFont, rowBg, Element.ALIGN_CENTER);
                addPdfCell(table, nvl(log.getEntityType()), dFont, rowBg, Element.ALIGN_CENTER);
                addPdfCell(table, nvl(log.getDetails()), dFont, rowBg, Element.ALIGN_LEFT);
                addPdfCell(table, nvl(log.getIpAddress()), dFont, rowBg, Element.ALIGN_LEFT);
                addPdfCell(table, nvl(log.getStatus()), dFont, rowBg, Element.ALIGN_CENTER);
            }

            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }
    }

    private void createCell(XSSFRow row, int col, String value, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(col);
        cell.setCellValue(value);
        if (style != null) cell.setCellStyle(style);
    }

    private void addPdfCell(PdfPTable table, String text, Font font, Color bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setBackgroundColor(bg);
        cell.setPadding(4);
        cell.setHorizontalAlignment(align);
        table.addCell(cell);
    }

    private String nvl(String s) {
        return s != null ? s : "";
    }
}
