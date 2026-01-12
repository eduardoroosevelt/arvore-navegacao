package com.example.menu.repository;

import com.example.menu.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    @Query("select distinct m from MenuItemEntity m " +
            "left join fetch m.requiredPermissions rp " +
            "left join fetch m.parent p " +
            "where m.enabled = true")
    List<MenuItemEntity> findAllEnabledWithPermissions();
}
