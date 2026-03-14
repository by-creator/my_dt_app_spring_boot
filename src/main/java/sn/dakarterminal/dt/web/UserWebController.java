package sn.dakarterminal.dt.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.dakarterminal.dt.dto.UserCreateDto;
import sn.dakarterminal.dt.service.RoleService;
import sn.dakarterminal.dt.service.UserService;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
public class UserWebController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("pageTitle", "Gestion des Utilisateurs");
        return "admin/users/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserCreateDto());
        model.addAttribute("roles", roleService.findAllActive());
        model.addAttribute("pageTitle", "Créer un Utilisateur");
        model.addAttribute("isEdit", false);
        return "admin/users/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String create(@Valid @ModelAttribute("userForm") UserCreateDto dto,
                         BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAllActive());
            model.addAttribute("pageTitle", "Créer un Utilisateur");
            model.addAttribute("isEdit", false);
            return "admin/users/form";
        }
        try {
            userService.create(dto);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Utilisateur '" + dto.getName() + "' créé avec succès.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String editForm(@PathVariable Long id, Model model) {
        var user = userService.findById(id);
        UserCreateDto dto = new UserCreateDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTwoFactorEnabled(user.getTwoFactorEnabled());
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getId());
        }
        model.addAttribute("userForm", dto);
        model.addAttribute("userId", id);
        model.addAttribute("roles", roleService.findAllActive());
        model.addAttribute("pageTitle", "Modifier l'Utilisateur");
        model.addAttribute("isEdit", true);
        return "admin/users/form";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("userForm") UserCreateDto dto,
                         BindingResult result, Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            model.addAttribute("roles", roleService.findAllActive());
            model.addAttribute("pageTitle", "Modifier l'Utilisateur");
            model.addAttribute("isEdit", true);
            return "admin/users/form";
        }
        try {
            userService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMsg", "Utilisateur mis à jour avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public String deactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deactivate(id);
            redirectAttributes.addFlashAttribute("successMsg", "Utilisateur désactivé.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("successMsg", "Utilisateur supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Impossible de supprimer cet utilisateur: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
