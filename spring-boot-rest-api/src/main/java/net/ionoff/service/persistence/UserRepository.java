package net.ionoff.service.persistence;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class UserRepository {
    
    public Map<String, UserDetails> getAll() {
        return Map.of("user", User.withUsername("user").password("").roles("USER").build(),
                "admin", User.withUsername("admin").password("").roles("USER", "ADMIN").build());
    }
    
    public Optional<UserDetails> findByUsername(String username) {
        return Optional.ofNullable(getAll().get(username));
    }
}
