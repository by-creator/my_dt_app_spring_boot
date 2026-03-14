package sn.dakarterminal.dt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.RattachementBl;
import sn.dakarterminal.dt.repository.RattachementBlRepository;
import sn.dakarterminal.dt.service.DematEmailService;

import java.util.Map;

@Controller
@RequestMapping("/demat")
@RequiredArgsConstructor
public class DematWebController {

    private final DematEmailService dematEmailService;
    private final RattachementBlRepository rattachementBlRepository;

    @GetMapping({"", "/"})
    public String index() {
        return "demat/index";
    }

    @GetMapping("/validation")
    public String validation() {
        return "demat/validation/index";
    }

    @GetMapping("/remise")
    public String remise() {
        return "demat/remise/index";
    }

    // ── POST: Demande de validation ───────────────────────────

    @PostMapping("/validation")
    @ResponseBody
    public ResponseEntity<Map<String, String>> submitValidation(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String numeroBl,
            @RequestParam(required = false) String maisonTransit,
            @RequestParam(required = false) MultipartFile fileBl,
            @RequestParam(required = false) MultipartFile fileBadShipping,
            @RequestParam(required = false) MultipartFile fileDeclaration) {

        // Save to DB
        RattachementBl rattachement = RattachementBl.builder()
                .nom(nom)
                .prenom(prenom)
                .email(email)
                .bl(numeroBl != null ? numeroBl : "")
                .maisonTransit(maisonTransit)
                .statut("EN_ATTENTE")
                .build();
        rattachementBlRepository.save(rattachement);

        // Send email with attachments
        dematEmailService.sendValidationEmail(nom, prenom, email, numeroBl, maisonTransit,
                fileBl, fileBadShipping, fileDeclaration);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    // ── POST: Demande de remise ───────────────────────────────

    @PostMapping("/remise")
    @ResponseBody
    public ResponseEntity<Map<String, String>> submitRemise(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String numeroBl,
            @RequestParam(required = false) String maisonTransit,
            @RequestParam(required = false) MultipartFile fileDemandeManuscrite,
            @RequestParam(required = false) MultipartFile fileBadShipping,
            @RequestParam(required = false) MultipartFile fileBl,
            @RequestParam(required = false) MultipartFile fileFacture,
            @RequestParam(required = false) MultipartFile fileDeclaration) {

        dematEmailService.sendRemiseEmail(nom, prenom, email, numeroBl, maisonTransit,
                fileDemandeManuscrite, fileBadShipping, fileBl, fileFacture, fileDeclaration);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
