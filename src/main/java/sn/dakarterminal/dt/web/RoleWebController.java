package sn.dakarterminal.dt.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.dakarterminal.dt.dto.RoleCreateDto;
import sn.dakarterminal.dt.service.RoleService;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
public class RoleWebController {

    private final RoleService roleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("pageTitle", "Gestion des Rôles");
        return "admin/roles/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("roleForm", new RoleCreateDto());
        model.addAttribute("pageTitle", "Créer un Rôle");
        model.addAttribute("isEdit", false);
        return "admin/roles/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("roleForm") RoleCreateDto dto,
                         BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Créer un Rôle");
            model.addAttribute("isEdit", false);
            return "admin/roles/form";
        }
        try {
            roleService.create(dto);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Rôle '" + dto.getName() + "' créé avec succès.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        var role = roleService.findById(id);
        RoleCreateDto dto = new RoleCreateDto();
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setActif(role.getActif());
        model.addAttribute("roleForm", dto);
        model.addAttribute("roleId", id);
        model.addAttribute("pageTitle", "Modifier le Rôle");
        model.addAttribute("isEdit", true);
        return "admin/roles/form";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("roleForm") RoleCreateDto dto,
                         BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roleId", id);
            model.addAttribute("pageTitle", "Modifier le Rôle");
            model.addAttribute("isEdit", true);
            return "admin/roles/form";
        }
        try {
            roleService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Rôle mis à jour avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roleService.delete(id);
            redirectAttributes.addFlashAttribute("successMsg", "Rôle supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Impossible de supprimer ce rôle: " + e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    @PostMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var role = roleService.toggleActive(id);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Rôle " + role.getName() + (Boolean.TRUE.equals(role.getActif()) ? " activé." : " désactivé."));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/roles";
    }
}
