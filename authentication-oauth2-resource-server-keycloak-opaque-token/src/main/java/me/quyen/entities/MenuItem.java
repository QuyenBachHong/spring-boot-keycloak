package me.quyen.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
@ToString
public class MenuItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private Long menuId;
    private String name;
    private String description;
    @Column(name = "type_name")
    private String type;
    @Column(name = "group_name")
    private String group;
    private BigDecimal price;

    public MenuItem(Long menuId, String name, String description
            , String type, String group, BigDecimal price) {
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.group = group;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MenuItem menuItem = (MenuItem) o;
        return id != null && Objects.equals(id, menuItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}