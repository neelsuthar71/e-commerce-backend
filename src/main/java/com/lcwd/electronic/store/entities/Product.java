package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table( name = "products")
public class Product {
    @Id
    private String productId;
    private String productTitle;
    @Column(length = 1000)
    private String getProductDescription;
    private double price;
    private double discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

}
