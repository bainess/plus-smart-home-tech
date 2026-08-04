package ru.yandex.practicum.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "reserved_quantity")
    private int reservedQuantity = 0;

    @Version
    private int version;

    @Transient
    public int getAvailableQuantity() {
        return Math.toIntExact(quantity - reservedQuantity);
    }
}
