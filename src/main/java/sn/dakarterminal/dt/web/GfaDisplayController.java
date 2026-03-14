package sn.dakarterminal.dt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.dakarterminal.dt.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.dakarterminal.dt.entity.GfaWifiSettings;
import sn.dakarterminal.dt.repository.GfaWifiSettingsRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import sn.dakarterminal.dt.entity.GfaWifiSettings;
import sn.dakarterminal.dt.repository.GfaWifiSettingsRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/gfa")
@RequiredArgsConstructor
public class GfaDisplayController {

    private static final String DEFAULT_WIFI_SSID = "DakarTerminal_WiFi";
    private static final String DEFAULT_WIFI_PASSWORD = "";

    private final GfaWifiSettingsRepository gfaWifiSettingsRepository;

    private final ServiceRepository serviceRepository;

    @GetMapping({"/display", "/display/"})
    public String display(Model model) {
        GfaWifiSettings settings = gfaWifiSettingsRepository.findById(1L)
                .orElseGet(() -> GfaWifiSettings.builder()
                        .ssid(DEFAULT_WIFI_SSID)
                        .password(DEFAULT_WIFI_PASSWORD)
                        .build());

        String wifiPayload = "WIFI:T:WPA;S:" + escapeWifi(settings.getSsid()) + ";P:" + escapeWifi(settings.getPassword()) + ";;";
        model.addAttribute("wifiSsid", settings.getSsid());
        model.addAttribute("wifiPassword", settings.getPassword());
        model.addAttribute("wifiQrData", URLEncoder.encode(wifiPayload, StandardCharsets.UTF_8));
        return "gfa/display/index";
    }

    private String escapeWifi(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace(",", "\\,")
                .replace(":", "\\:")
                .replace("\"", "\\\"");
    }

    @GetMapping({"/ticket", "/ticket/"})
    public String ticket(Model model) {
        model.addAttribute("services", serviceRepository.findByActifTrue());
        return "gfa/ticket/index";
    }
}
