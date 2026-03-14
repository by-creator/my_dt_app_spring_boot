package sn.dakarterminal.dt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleCreateDto {

    @NotBlank(message = "Le nom du rôle est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Le nom doit être en majuscules (ex: ADMIN, SUPER_U)")
    private String name;

    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères")
    private String description;

    private Boolean actif = true;
}
