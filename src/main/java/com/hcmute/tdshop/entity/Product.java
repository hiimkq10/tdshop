package com.hcmute.tdshop.entity;

import java.time.LocalDateTime;
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
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT(19)")
  private Long id;

  @Column(name = "sku", columnDefinition = "NVARCHAR(100)", nullable = false, unique = true)
  private String sku;

  @Column(name = "name", columnDefinition = "NVARCHAR(100)", nullable = false, unique = true)
  private String name;

  @Column(name = "price", nullable = false)
  private double price;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "short_description")
  private String shortDescription;

  @Column(name = "total", nullable = false)
  private int total;

  @Column(name = "selAmount", nullable = false)
  private int selAmount;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "deleted_at", nullable = false)
  private LocalDateTime deletedAt;

  @ManyToOne
  @JoinColumn(name = "product_status", nullable = false)
  private ProductStatus status;

  @ManyToMany(mappedBy = "setOfProducts", fetch = FetchType.LAZY)
  private Set<Category> setOfCategories;

  @ManyToOne
  @JoinColumn(name = "brand_id", nullable = false)
  private Brand brand;

  @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Image> setOfImages;

  @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<ProductAttribute> setOfProductAttributes;

  @ManyToMany
  @JoinTable(name = "product_configuration",
      joinColumns = {@JoinColumn(name = "product_id")}, inverseJoinColumns = {
      @JoinColumn(name = "variation_option_id")})
  private Set<VariationOption> setOfVariationOptions;
}
