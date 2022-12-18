package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Brand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
  private String name;

  @Column(name = "logo", unique = true)
  private String logoUrl;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.PERSIST)
  @JsonIgnore
  private Set<Product> setOfProducts;
  
  @PreRemove
  private void preRemove() {
    setOfProducts.forEach(product -> product.setBrand(null));
  }
}
