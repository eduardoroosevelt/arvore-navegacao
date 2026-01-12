package com.example.menu.web;

import com.example.menu.entity.MenuItemEntity;
import com.example.menu.entity.PermissionEntity;
import com.example.menu.entity.UserEntity;
import com.example.menu.repository.MenuItemRepository;
import com.example.menu.repository.PermissionRepository;
import com.example.menu.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository,
                           PermissionRepository permissionRepository,
                           MenuItemRepository menuItemRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.menuItemRepository = menuItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity createUser(@RequestBody CreateUserRequest request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoles());
        user.setEnabled(request.isEnabled());
        return userRepository.save(user);
    }

    @PostMapping("/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    public PermissionEntity createPermission(@RequestBody CreatePermissionRequest request) {
        PermissionEntity permission = new PermissionEntity(request.getCode());
        return permissionRepository.save(permission);
    }

    @PostMapping("/menu-items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemEntity createMenuItem(@RequestBody CreateMenuItemRequest request) {
        MenuItemEntity menuItem = new MenuItemEntity();
        menuItem.setCode(request.getCode());
        menuItem.setLabel(request.getLabel());
        menuItem.setRoute(request.getRoute());
        menuItem.setIcon(request.getIcon());
        menuItem.setOrderIndex(request.getOrderIndex());
        menuItem.setEnabled(request.isEnabled());
        if (request.getParentId() != null) {
            Optional<MenuItemEntity> parent = menuItemRepository.findById(request.getParentId());
            parent.ifPresent(menuItem::setParent);
        }
        return menuItemRepository.save(menuItem);
    }

    @PostMapping("/users/{userId}/permissions/{permissionId}")
    public UserEntity addPermissionToUser(@PathVariable Long userId, @PathVariable Long permissionId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow();
        user.getPermissions().add(permission);
        return userRepository.save(user);
    }

    @PostMapping("/menu-items/{menuId}/permissions/{permissionId}")
    public MenuItemEntity addPermissionToMenuItem(@PathVariable Long menuId, @PathVariable Long permissionId) {
        MenuItemEntity menuItem = menuItemRepository.findById(menuId).orElseThrow();
        PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow();
        menuItem.getRequiredPermissions().add(permission);
        return menuItemRepository.save(menuItem);
    }

    public static class CreateUserRequest {
        private String username;
        private String password;
        private String roles;
        private boolean enabled = true;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class CreatePermissionRequest {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class CreateMenuItemRequest {
        private String code;
        private String label;
        private String route;
        private String icon;
        private Long parentId;
        private Integer orderIndex = 0;
        private boolean enabled = true;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public Integer getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
