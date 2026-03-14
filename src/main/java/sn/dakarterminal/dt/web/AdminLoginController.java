package sn.dakarterminal.dt.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLoginController {

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Email ou mot de passe incorrect.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Vous avez été déconnecté avec succès.");
        }
        return "auth/login";
    }
}
