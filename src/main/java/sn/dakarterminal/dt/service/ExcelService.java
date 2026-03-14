package sn.dakarterminal.dt.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import sn.dakarterminal.dt.entity.Facturation;
import sn.dakarterminal.dt.entity.Machine;
import sn.dakarterminal.dt.entity.Yard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ExcelService {

    public byte[] generateFacturationReport(List<Facturation> facturations) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Facturations");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "N° BL", "N° Facture", "Client", "Navire", "Date Facture",
                    "Montant HT", "TVA", "Montant TTC", "Statut"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Facturation f : facturations) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(f.getId() != null ? f.getId() : 0);
                row.createCell(1).setCellValue(f.getNumeroBl() != null ? f.getNumeroBl() : "");
                row.createCell(2).setCellValue(f.getNumeroFacture() != null ? f.getNumeroFacture() : "");
                row.createCell(3).setCellValue(f.getClient() != null ? f.getClient() : "");
                row.createCell(4).setCellValue(f.getNavire() != null ? f.getNavire() : "");
                row.createCell(5).setCellValue(f.getDateFacture() != null ? f.getDateFacture().toString() : "");
                row.createCell(6).setCellValue(f.getMontantHt() != null ? f.getMontantHt().doubleValue() : 0);
                row.createCell(7).setCellValue(f.getMontantTva() != null ? f.getMontantTva().doubleValue() : 0);
                row.createCell(8).setCellValue(f.getMontantTtc() != null ? f.getMontantTtc().doubleValue() : 0);
                row.createCell(9).setCellValue(f.getStatut() != null ? f.getStatut() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generating facturation Excel report", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    public byte[] generateYardReport(List<Yard> yards) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Yard");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "N° Conteneur", "Type", "Etat", "Position", "Navire", "Client", "BL",
                    "Date Arrivée", "Statut"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Yard y : yards) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(y.getId() != null ? y.getId() : 0);
                row.createCell(1).setCellValue(y.getNumeroConteneur() != null ? y.getNumeroConteneur() : "");
                row.createCell(2).setCellValue(y.getTypeConteneur() != null ? y.getTypeConteneur() : "");
                row.createCell(3).setCellValue(y.getEtat() != null ? y.getEtat() : "");
                row.createCell(4).setCellValue(y.getPosition() != null ? y.getPosition() : "");
                row.createCell(5).setCellValue(y.getNavire() != null ? y.getNavire() : "");
                row.createCell(6).setCellValue(y.getClient() != null ? y.getClient() : "");
                row.createCell(7).setCellValue(y.getBl() != null ? y.getBl() : "");
                row.createCell(8).setCellValue(y.getDateArrivee() != null ? y.getDateArrivee().toString() : "");
                row.createCell(9).setCellValue(y.getStatut() != null ? y.getStatut() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generating yard Excel report", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    public byte[] generateMachineReport(List<Machine> machines) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Machines");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "N° Série", "Modèle", "Type", "Utilisateur", "Service", "Site", "Etat"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Machine m : machines) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(m.getId() != null ? m.getId() : 0);
                row.createCell(1).setCellValue(m.getNumeroSerie() != null ? m.getNumeroSerie() : "");
                row.createCell(2).setCellValue(m.getModele() != null ? m.getModele() : "");
                row.createCell(3).setCellValue(m.getType() != null ? m.getType().name() : "");
                row.createCell(4).setCellValue(m.getUtilisateur() != null ? m.getUtilisateur() : "");
                row.createCell(5).setCellValue(m.getService() != null ? m.getService() : "");
                row.createCell(6).setCellValue(m.getSite() != null ? m.getSite() : "");
                row.createCell(7).setCellValue(m.getEtat() != null ? m.getEtat() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generating machine Excel report", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
