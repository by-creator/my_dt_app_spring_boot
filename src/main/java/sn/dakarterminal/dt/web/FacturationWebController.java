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
}
