package se.iths.erikthorell.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String name;

    private BigDecimal price;

    private int quantity;

    private BigDecimal lineTotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder customerOrder;
}