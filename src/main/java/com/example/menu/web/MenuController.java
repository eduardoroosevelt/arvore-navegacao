package com.example.menu.web;

import com.example.menu.dto.MenuItemResponseDTO;
import com.example.menu.service.MenuService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<MenuItemResponseDTO> getMenu(Authentication authentication) {
        return menuService.getMenuForCurrentUser(authentication);
    }
}
