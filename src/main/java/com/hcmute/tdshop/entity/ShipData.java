package com.hcmute.tdshop.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "ship_order_id")
  private String shipOrderId;

  @Column(name = "ordered_at")
  private LocalDateTime orderedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private ShopOrder order;
}
