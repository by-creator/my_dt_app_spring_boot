package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.Facturation;
import sn.dakarterminal.dt.entity.FacturationStaging;
import sn.dakarterminal.dt.repository.FacturationRepository;
import sn.dakarterminal.dt.repository.FacturationStagingRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FacturationImportService {

    private final FacturationStagingRepository stagingRepository;
    private final FacturationRepository facturationRepository;

    public String importFromExcel(MultipartFile file) throws IOException {
        String batchId = UUID.randomUUID().toString();
        List<FacturationStaging> stagings = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                FacturationStaging staging = FacturationStaging.builder()
                        .numeroBl(getCellValue(row, 0))
                        .numeroFacture(getCellValue(row, 1))
                        .client(getCellValue(row, 2))
                        .navire(getCellValue(row, 3))
                        .armateur(getCellValue(row, 4))
                        .batchId(batchId)
                        .processed(false)
                        .build();

                stagings.add(staging);
            }
        }

        stagingRepository.saveAll(stagings);
        log.info("Imported {} records with batchId: {}", stagings.size(), batchId);
        return batchId;
    }

    public void consolidate(String batchId) {
        List<FacturationStaging> stagings = stagingRepository.findByBatchId(batchId);

        for (FacturationStaging staging : stagings) {
            try {
                Facturation facturation = Facturation.builder()
                        .numeroBl(staging.getNumeroBl())
                        .numeroFacture(staging.getNumeroFacture())
                        .client(staging.getClient())
                        .navire(staging.getNavire())
                        .armateur(staging.getArmateur())
                        .statut(staging.getStatut())
                        .build();

                facturationRepository.save(facturation);
                staging.setProcessed(true);
                stagingRepository.save(staging);
            } catch (Exception e) {
                staging.setImportErrors(e.getMessage());
                stagingRepository.save(staging);
                log.error("Error consolidating staging record {}: {}", staging.getId(), e.getMessage());
            }
        }

        log.info("Consolidated batch: {}", batchId);
    }

    private String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }
}
