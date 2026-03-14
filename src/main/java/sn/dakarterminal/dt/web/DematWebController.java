package sn.dakarterminal.dt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.service.DematEmailService;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/demat")
@RequiredArgsConstructor
public class DematWebController {

    private final DematEmailService dematEmailService;

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

        Map<String, String> fichiers = new LinkedHashMap<>();
        fichiers.put("BL",           fileInfo(fileBl));
        fichiers.put("BAD SHIPPING", fileInfo(fileBadShipping));
        fichiers.put("DECLARATION",  fileInfo(fileDeclaration));

        dematEmailService.sendValidationEmail(nom, prenom, email, numeroBl, maisonTransit, fichiers);

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

        Map<String, String> fichiers = new LinkedHashMap<>();
        fichiers.put("DEMANDE MANUSCRITE", fileInfo(fileDemandeManuscrite));
        fichiers.put("BAD SHIPPING",       fileInfo(fileBadShipping));
        fichiers.put("BL",                 fileInfo(fileBl));
        fichiers.put("FACTURE",            fileInfo(fileFacture));
        fichiers.put("DECLARATION",        fileInfo(fileDeclaration));

        dematEmailService.sendRemiseEmail(nom, prenom, email, numeroBl, maisonTransit, fichiers);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    // ── Helpers ───────────────────────────────────────────────

    private static String fileInfo(MultipartFile f) {
        if (f == null || f.isEmpty()) return null;
        String name = f.getOriginalFilename();
        long kb = f.getSize() / 1024;
        return (name != null ? name : "fichier") + " (" + kb + " Ko)";
    }
}
