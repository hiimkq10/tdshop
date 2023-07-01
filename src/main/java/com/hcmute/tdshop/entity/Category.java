package com.hcmute.tdshop.entity;

import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(50)", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
  private List<Category> children;

  @ManyToOne
  @JoinColumn(name = "master_category_id", nullable = false)
  private MasterCategory masterCategory;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinTable(name = "product_category",
      joinColumns = {@JoinColumn(name = "category_id")}, inverseJoinColumns = {@JoinColumn(name = "product_id")})
  private Set<Product> setOfProducts;

  @ManyToMany(mappedBy = "setOfCategories", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  private Set<Promotion> setOfPromotions;

  @PreRemove
  private void preRemove() {
    masterCategory.getSetOfCategories().remove(this);
    children.forEach(child -> child.setParent(null));
    setOfProducts.forEach(product -> product.getSetOfCategories().remove(this));
    setOfPromotions.forEach(promotion -> promotion.getSetOfCategories().remove(this));
  }
}
