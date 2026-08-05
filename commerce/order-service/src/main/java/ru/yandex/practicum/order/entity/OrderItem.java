package ru.yandex.practicum.order.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "product_id")
    Long productId;

    @Column(name = "productName")
    String productName;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "price")
    BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;
}
