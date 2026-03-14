package sn.dakarterminal.dt.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/facturation")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
public class FacturationWebController {

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

    @GetMapping("/gfa-admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String gfaAdmin(Model model) {
        model.addAttribute("pageTitle", "Gfa Admin");
        return "facturation/gfa-admin/index";
    }
}
