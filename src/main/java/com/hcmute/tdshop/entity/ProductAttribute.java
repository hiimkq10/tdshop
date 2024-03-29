package com.hcmute.tdshop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttribute {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "value", nullable = false)
  private String value;

  @ManyToOne
  @JoinColumn(name = "attribute_id", nullable = false)
  private Attribute attribute;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @PreRemove
  public void preRemove() {
    product.getSetOfProductAttributes().remove(this);
//    attribute.getSetOfProductAttributes().remove(this);
  }
}
