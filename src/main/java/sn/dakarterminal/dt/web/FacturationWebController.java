package sn.dakarterminal.dt.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.dakarterminal.dt.entity.GfaWifiSettings;
import sn.dakarterminal.dt.repository.GfaWifiSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.dakarterminal.dt.entity.GfaWifiSettings;
import sn.dakarterminal.dt.repository.GfaWifiSettingsRepository;


@Controller
@RequestMapping("/facturation")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
@RequiredArgsConstructor
public class FacturationWebController {

    private static final String DEFAULT_WIFI_SSID = "DakarTerminal_WiFi";
    private static final String DEFAULT_WIFI_PASSWORD = "";

    private final GfaWifiSettingsRepository gfaWifiSettingsRepository;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Facturation");
        return "facturation/dashboard/index";
    }

    @GetMapping("/guichet")
    public String guichet(Model model) {
        model.addAttribute("pageTitle", "Guichet");
        return "facturation/guichet/index";
    }

    @GetMapping("/validations")
    public String validations(Model model) {
        model.addAttribute("pageTitle", "Gestion des validations");
        return "facturation/validations/index";
    }

    @GetMapping("/remises")
    public String remises(Model model) {
        model.addAttribute("pageTitle", "Gestion de remises");
        return "facturation/remises/index";
    }

    @GetMapping("/unify")
    public String unify(Model model) {
        model.addAttribute("pageTitle", "Gestion Unify");
        return "facturation/unify/index";
    }

    @GetMapping("/ies")
    public String ies(Model model) {
        model.addAttribute("pageTitle", "Gestion IES");
        return "facturation/ies/index";
    }

    @GetMapping("/gfa-admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String gfaAdmin(Model model) {
        model.addAttribute("pageTitle", "Gfa Admin");
        GfaWifiSettings settings = gfaWifiSettingsRepository.findById(1L)
                .orElseGet(() -> GfaWifiSettings.builder()
                        .ssid(DEFAULT_WIFI_SSID)
                        .password(DEFAULT_WIFI_PASSWORD)
                        .build());
        model.addAttribute("wifiSettings", settings);
        return "facturation/gfa-admin/index";
    }

    @PostMapping("/gfa-admin/wifi-settings")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String saveWifiSettings(@RequestParam("ssid") String ssid,
                                   @RequestParam("password") String password,
                                   RedirectAttributes redirectAttributes) {
        GfaWifiSettings settings = gfaWifiSettingsRepository.findById(1L)
                .orElseGet(() -> GfaWifiSettings.builder().id(1L).build());

        settings.setSsid(ssid == null ? "" : ssid.trim());
        settings.setPassword(password == null ? "" : password.trim());
        gfaWifiSettingsRepository.save(settings);

        redirectAttributes.addFlashAttribute("successMessage", "Paramètres Wi-Fi mis à jour avec succès.");
        return "redirect:/facturation/gfa-admin";
    }
}
