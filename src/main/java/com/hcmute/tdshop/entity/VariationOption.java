package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class VariationOption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "value", nullable = false)
  private String value;

  @ManyToOne
  @JoinColumn(name = "variation_id", nullable = false)
  @JsonIgnore
  private Variation variation;

  @ManyToMany(mappedBy = "setOfVariationOptions", cascade = CascadeType.PERSIST)
  @JsonIgnore
  private Set<Product> setOfProducts;

  @PreRemove
  private void preRemove() {
    variation.getSetOfVariationOptions().remove(this);
    setOfProducts.forEach(product -> product.getSetOfVariationOptions().remove(this));
  }
}
