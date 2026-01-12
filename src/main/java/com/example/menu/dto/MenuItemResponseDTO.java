package com.example.menu.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuItemResponseDTO {
    private Long id;
    private String code;
    private String label;
    private String route;
    private String icon;
    private List<MenuItemResponseDTO> children = new ArrayList<>();

    public MenuItemResponseDTO() {
    }

    public MenuItemResponseDTO(Long id, String code, String label, String route, String icon) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.route = route;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<MenuItemResponseDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItemResponseDTO> children) {
        this.children = children;
    }
}
