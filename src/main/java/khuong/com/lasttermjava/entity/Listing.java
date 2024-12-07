package khuong.com.lasttermjava.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import lombok.Data;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private BigDecimal price;
    private String listingType; // sale or rent

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Image> images;
}
