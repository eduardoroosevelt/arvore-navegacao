package com.example.menu.service;

import com.example.menu.dto.MenuItemResponseDTO;
import com.example.menu.entity.MenuItemEntity;
import com.example.menu.entity.PermissionEntity;
import com.example.menu.repository.MenuItemRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuItemRepository menuItemRepository;

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItemResponseDTO> getMenuForCurrentUser(Authentication authentication) {
        Set<String> userPermissions = resolveUserPermissions(authentication);
        List<MenuItemEntity> enabledItems = menuItemRepository.findAllEnabledWithPermissions();
        Map<Long, MenuNode> nodeMap = new HashMap<>();
        for (MenuItemEntity item : enabledItems) {
            nodeMap.put(item.getId(), new MenuNode(item));
        }
        List<MenuNode> roots = new ArrayList<>();
        for (MenuItemEntity item : enabledItems) {
            MenuNode node = nodeMap.get(item.getId());
            MenuItemEntity parent = item.getParent();
            if (parent != null && nodeMap.containsKey(parent.getId())) {
                nodeMap.get(parent.getId()).children.add(node);
            } else {
                roots.add(node);
            }
        }
        Comparator<MenuNode> comparator = Comparator
                .comparing(MenuNode::orderIndex, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(MenuNode::label, Comparator.nullsLast(String::compareToIgnoreCase));
        sortRecursively(roots, comparator);
        List<MenuItemResponseDTO> response = new ArrayList<>();
        for (MenuNode root : roots) {
            filterNode(root, userPermissions).ifPresent(response::add);
        }
        return response;
    }

    private void sortRecursively(List<MenuNode> nodes, Comparator<MenuNode> comparator) {
        nodes.sort(comparator);
        for (MenuNode node : nodes) {
            sortRecursively(node.children, comparator);
        }
    }

    private Optional<MenuItemResponseDTO> filterNode(MenuNode node, Set<String> userPermissions) {
        boolean accessible = isAccessible(node.item, userPermissions);
        List<MenuItemResponseDTO> filteredChildren = new ArrayList<>();
        for (MenuNode child : node.children) {
            filterNode(child, userPermissions).ifPresent(filteredChildren::add);
        }
        if (!accessible && filteredChildren.isEmpty()) {
            return Optional.empty();
        }
        String route = accessible ? node.item.getRoute() : null;
        MenuItemResponseDTO dto = new MenuItemResponseDTO(
                node.item.getId(),
                node.item.getCode(),
                node.item.getLabel(),
                route,
                node.item.getIcon()
        );
        dto.setChildren(filteredChildren);
        return Optional.of(dto);
    }

    private boolean isAccessible(MenuItemEntity item, Set<String> userPermissions) {
        Set<String> required = item.getRequiredPermissions().stream()
                .map(PermissionEntity::getCode)
                .map(code -> code == null ? null : code.toUpperCase())
                .filter(code -> code != null && !code.isBlank())
                .collect(Collectors.toSet());
        return required.isEmpty() || userPermissions.containsAll(required);
    }

    private Set<String> resolveUserPermissions(Authentication authentication) {
        Set<String> permissions = new HashSet<>();
        if (authentication == null) {
            return permissions;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String value = authority.getAuthority();
            if (!value.startsWith("ROLE_")) {
                permissions.add(value);
            }
        }
        return permissions;
    }

    private static class MenuNode {
        private final MenuItemEntity item;
        private final List<MenuNode> children = new ArrayList<>();

        private MenuNode(MenuItemEntity item) {
            this.item = item;
        }

        private Integer orderIndex() {
            return item.getOrderIndex();
        }

        private String label() {
            return item.getLabel();
        }
    }
}
