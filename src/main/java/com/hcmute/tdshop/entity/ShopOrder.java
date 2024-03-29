package com.hcmute.tdshop.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "ordered_at", nullable = false)
  private LocalDateTime orderedAt;

  @ManyToOne
  @JoinColumn(name = "payment_method_id", nullable = false)
  private PaymentMethod paymentMethod;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "ship_price")
  private double shipPrice;

  @ManyToOne
  @JoinColumn(name = "ship_id", nullable = false)
  private Ship ship;

  @ManyToOne
  @JoinColumn(name = "order_status_id", nullable = false)
  private OrderStatus orderStatus;

  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
  private Set<OrderDetail> setOfOrderDetails;

  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
  private List<ShipData> shipData;
}
