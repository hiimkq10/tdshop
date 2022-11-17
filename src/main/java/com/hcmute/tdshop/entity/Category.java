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
  @Column(name = "id", columnDefinition = "BIGINT(19)")
  private Long id;

  @Column(name = "name", columnDefinition = "NVARCHAR(50)", nullable = false, unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<Category> children;

  @ManyToOne
  @JoinColumn(name = "master_category_id", nullable = false)
  private MasterCategory masterCategory;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "product_category",
      joinColumns = {@JoinColumn(name = "category_id")}, inverseJoinColumns = {@JoinColumn(name = "product_id")})
  private Set<Product> setOfProducts;

  @ManyToMany(mappedBy = "setOfCategories", fetch = FetchType.LAZY)
  private Set<Promotion> setOfPromotions;
}
