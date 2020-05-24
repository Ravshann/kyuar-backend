package uz.kyuar.security.services;

import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedUser {


    public static String getUsername() {
        return getUserPrinciple().getUsername();
    }

    public static UserPrinciple getUserPrinciple() {
        return (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isAdmin() {
        return getUserPrinciple().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }

    public static boolean hasRole(String role) {
        return getUserPrinciple().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(role));
    }
}
