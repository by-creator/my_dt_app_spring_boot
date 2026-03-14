package sn.dakarterminal.dt.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gfa")
public class GfaDisplayController {

    @GetMapping({"/display", "/display/"})
    public String display() {
        return "gfa/display/index";
    }

    @GetMapping({"/ticket", "/ticket/"})
    public String ticket() {
        return "gfa/ticket/index";
    }
}
