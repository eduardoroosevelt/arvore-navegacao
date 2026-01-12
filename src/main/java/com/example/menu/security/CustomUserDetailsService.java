package com.example.menu.security;

import com.example.menu.entity.PermissionEntity;
import com.example.menu.entity.UserEntity;
import com.example.menu.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                true,
                true,
                true,
                buildAuthorities(userEntity.getRoles(), userEntity.getPermissions())
        );
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(String roles, Set<PermissionEntity> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null && !roles.isBlank()) {
            String[] values = roles.split(",");
            for (String value : values) {
                String trimmed = value.trim();
                if (!trimmed.isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority(trimmed));
                }
            }
        }
        List<GrantedAuthority> permissionAuthorities = permissions.stream()
                .map(PermissionEntity::getCode)
                .map(code -> new SimpleGrantedAuthority(code.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
        authorities.addAll(permissionAuthorities);
        return authorities;
    }
}
