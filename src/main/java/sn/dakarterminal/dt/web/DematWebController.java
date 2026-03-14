package sn.dakarterminal.dt.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demat")
public class DematWebController {

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
}
