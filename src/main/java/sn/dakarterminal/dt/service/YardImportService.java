package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.Yard;
import sn.dakarterminal.dt.entity.YardStaging;
import sn.dakarterminal.dt.repository.YardRepository;
import sn.dakarterminal.dt.repository.YardStagingRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class YardImportService {

    private final YardStagingRepository stagingRepository;
    private final YardRepository yardRepository;

    public String importFromExcel(MultipartFile file) throws IOException {
        String batchId = UUID.randomUUID().toString();
        List<YardStaging> stagings = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                YardStaging staging = YardStaging.builder()
                        .numeroConteneur(getCellValue(row, 0))
                        .typeConteneur(getCellValue(row, 1))
                        .etat(getCellValue(row, 2))
                        .position(getCellValue(row, 3))
                        .navire(getCellValue(row, 4))
                        .client(getCellValue(row, 5))
                        .bl(getCellValue(row, 6))
                        .batchId(batchId)
                        .processed(false)
                        .build();

                stagings.add(staging);
            }
        }

        stagingRepository.saveAll(stagings);
        log.info("Imported {} yard records with batchId: {}", stagings.size(), batchId);
        return batchId;
    }

    public void consolidate(String batchId) {
        List<YardStaging> stagings = stagingRepository.findByBatchId(batchId);

        for (YardStaging staging : stagings) {
            try {
                Yard yard = Yard.builder()
                        .numeroConteneur(staging.getNumeroConteneur())
                        .typeConteneur(staging.getTypeConteneur())
                        .etat(staging.getEtat())
                        .position(staging.getPosition())
                        .navire(staging.getNavire())
                        .client(staging.getClient())
                        .bl(staging.getBl())
                        .statut("EN_PARC")
                        .build();

                yardRepository.save(yard);
                staging.setProcessed(true);
                stagingRepository.save(staging);
            } catch (Exception e) {
                staging.setImportErrors(e.getMessage());
                stagingRepository.save(staging);
                log.error("Error consolidating yard staging {}: {}", staging.getId(), e.getMessage());
            }
        }
    }

    private String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> null;
        };
    }
}
