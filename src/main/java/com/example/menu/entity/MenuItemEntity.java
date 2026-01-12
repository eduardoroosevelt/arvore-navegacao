package com.example.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_items")
public class MenuItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String label;

    private String route;

    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MenuItemEntity parent;

    @Column(nullable = false)
    private Integer orderIndex = 0;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "menu_item_permissions",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> requiredPermissions = new HashSet<>();

    public Long getId() {
        return id;
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

    public MenuItemEntity getParent() {
        return parent;
    }

    public void setParent(MenuItemEntity parent) {
        this.parent = parent;
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

    public Set<PermissionEntity> getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(Set<PermissionEntity> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }
}
