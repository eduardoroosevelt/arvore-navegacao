package com.example.menu.config;

import com.example.menu.entity.MenuItemEntity;
import com.example.menu.entity.PermissionEntity;
import com.example.menu.entity.UserEntity;
import com.example.menu.repository.MenuItemRepository;
import com.example.menu.repository.PermissionRepository;
import com.example.menu.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PermissionRepository permissionRepository,
                           MenuItemRepository menuItemRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.menuItemRepository = menuItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        PermissionEntity dashboardRead = permissionRepository.save(new PermissionEntity("DASHBOARD_READ"));
        PermissionEntity usersRead = permissionRepository.save(new PermissionEntity("USERS_READ"));
        PermissionEntity usersWrite = permissionRepository.save(new PermissionEntity("USERS_WRITE"));
        PermissionEntity reportsRead = permissionRepository.save(new PermissionEntity("REPORTS_READ"));

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles("ROLE_ADMIN");
        admin.setPermissions(Set.of(dashboardRead, usersRead, usersWrite, reportsRead));
        userRepository.save(admin);

        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles("ROLE_USER");
        user.setPermissions(Set.of(dashboardRead, reportsRead));
        userRepository.save(user);

        MenuItemEntity dashboard = new MenuItemEntity();
        dashboard.setCode("MENU.DASHBOARD");
        dashboard.setLabel("Dashboard");
        dashboard.setRoute("/dashboard");
        dashboard.setIcon("home");
        dashboard.setOrderIndex(0);
        dashboard.setRequiredPermissions(Set.of(dashboardRead));
        menuItemRepository.save(dashboard);

        MenuItemEntity usersRoot = new MenuItemEntity();
        usersRoot.setCode("MENU.USERS");
        usersRoot.setLabel("Usuários");
        usersRoot.setOrderIndex(1);
        usersRoot.setRequiredPermissions(Set.of(usersRead));
        menuItemRepository.save(usersRoot);

        MenuItemEntity usersList = new MenuItemEntity();
        usersList.setCode("MENU.USERS.LIST");
        usersList.setLabel("Listar usuários");
        usersList.setRoute("/users");
        usersList.setParent(usersRoot);
        usersList.setOrderIndex(0);
        usersList.setRequiredPermissions(Set.of(usersRead));
        menuItemRepository.save(usersList);

        MenuItemEntity usersCreate = new MenuItemEntity();
        usersCreate.setCode("MENU.USERS.CREATE");
        usersCreate.setLabel("Criar usuário");
        usersCreate.setRoute("/users/new");
        usersCreate.setParent(usersRoot);
        usersCreate.setOrderIndex(1);
        usersCreate.setRequiredPermissions(Set.of(usersWrite));
        menuItemRepository.save(usersCreate);

        MenuItemEntity reportsRoot = new MenuItemEntity();
        reportsRoot.setCode("MENU.REPORTS");
        reportsRoot.setLabel("Relatórios");
        reportsRoot.setOrderIndex(2);
        reportsRoot.setRequiredPermissions(Set.of(reportsRead));
        menuItemRepository.save(reportsRoot);

        MenuItemEntity reportsDaily = new MenuItemEntity();
        reportsDaily.setCode("MENU.REPORTS.DAILY");
        reportsDaily.setLabel("Relatório diário");
        reportsDaily.setRoute("/reports/daily");
        reportsDaily.setParent(reportsRoot);
        reportsDaily.setOrderIndex(0);
        reportsDaily.setRequiredPermissions(Set.of(reportsRead));
        menuItemRepository.save(reportsDaily);
    }
}
