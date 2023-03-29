package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "price", nullable = false)
  private double price;

  @Column(name = "discount_rate", nullable = false)
  private double discountRate;

  @Column(name = "final_price", nullable = false)
  private double finalPrice;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private ShopOrder order;

  @Formula("finalPrice * quantity")
  @JsonIgnore
  private double total;

  public OrderDetail(Long id, double price, double discountRate, double finalPrice, int quantity, Product product, ShopOrder order) {
    this.id = id;
    this.price = price;
    this.discountRate = discountRate;
    this.finalPrice = finalPrice;
    this.quantity = quantity;
    this.product = product;
    this.order = order;
  }
}
