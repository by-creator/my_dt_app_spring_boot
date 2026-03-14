package sn.dakarterminal.dt.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectUrl = "/admin/dashboard";

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_FACTURATION".equals(authority.getAuthority())) {
                redirectUrl = "/facturation/dashboard";
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
