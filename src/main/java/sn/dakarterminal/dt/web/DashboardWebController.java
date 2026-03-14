package sn.dakarterminal.dt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.dakarterminal.dt.repository.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
public class DashboardWebController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditLogRepository auditLogRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalRoles", roleRepository.count());
        model.addAttribute("totalAuditLogs", auditLogRepository.count());
        model.addAttribute("logsToday",
                auditLogRepository.countByCreatedAtAfter(LocalDateTime.now().toLocalDate().atStartOfDay()));
        model.addAttribute("recentLogs", auditLogRepository.findTop10ByOrderByCreatedAtDesc());
        model.addAttribute("pageTitle", "Tableau de bord");
        return "admin/dashboard/index";
    }
}
