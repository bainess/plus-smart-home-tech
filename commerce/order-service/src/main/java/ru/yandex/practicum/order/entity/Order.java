package ru.yandex.practicum.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.order.feign.model.Pending_Reason;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "customer_name")
    String customerName;

    @Column(name = "customer_email")
    String customerEmail;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "total_price")
    BigDecimal totalPrice;

    @Column(name = "status_details")
    @Enumerated(EnumType.STRING)
    Pending_Reason statusDetails;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

}
