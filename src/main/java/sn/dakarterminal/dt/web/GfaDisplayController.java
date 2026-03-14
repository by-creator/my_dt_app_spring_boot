package sn.dakarterminal.dt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.dakarterminal.dt.repository.ServiceRepository;

@Controller
@RequestMapping("/gfa")
@RequiredArgsConstructor
public class GfaDisplayController {

    private final ServiceRepository serviceRepository;

    @GetMapping({"/display", "/display/"})
    public String display() {
        return "gfa/display/index";
    }

    @GetMapping({"/ticket", "/ticket/"})
    public String ticket(Model model) {
        model.addAttribute("services", serviceRepository.findByActifTrue());
        return "gfa/ticket/index";
    }
}
