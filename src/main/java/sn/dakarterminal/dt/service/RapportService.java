package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.Facturation;
import sn.dakarterminal.dt.entity.RapportInfosFacturation;
import sn.dakarterminal.dt.entity.Yard;
import sn.dakarterminal.dt.repository.FacturationRepository;
import sn.dakarterminal.dt.repository.YardRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RapportService {

    private final FacturationRepository facturationRepository;
    private final YardRepository yardRepository;
    private final ExcelService excelService;
    private final PdfService pdfService;

    public RapportInfosFacturation generateRapportFacturation(LocalDate debut, LocalDate fin, Long generatedBy) {
        List<Facturation> facturations = facturationRepository.findByDateFactureBetween(debut, fin);

        BigDecimal totalHt = facturations.stream()
                .filter(f -> f.getMontantHt() != null)
                .map(Facturation::getMontantHt)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTva = facturations.stream()
                .filter(f -> f.getMontantTva() != null)
                .map(Facturation::getMontantTva)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTtc = facturations.stream()
                .filter(f -> f.getMontantTtc() != null)
                .map(Facturation::getMontantTtc)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RapportInfosFacturation.builder()
                .periodeDebut(debut)
                .periodeFin(fin)
                .totalFactures(facturations.size())
                .totalMontantHt(totalHt)
                .totalMontantTva(totalTva)
                .totalMontantTtc(totalTtc)
                .generatedBy(generatedBy)
                .build();
    }

    public byte[] exportFacturationsToExcel(LocalDate debut, LocalDate fin) {
        List<Facturation> facturations = facturationRepository.findByDateFactureBetween(debut, fin);
        return excelService.generateFacturationReport(facturations);
    }

    public byte[] exportYardToExcel(LocalDate debut, LocalDate fin) {
        List<Yard> yards = yardRepository.findByDateArriveeBetween(debut, fin);
        return excelService.generateYardReport(yards);
    }
}
