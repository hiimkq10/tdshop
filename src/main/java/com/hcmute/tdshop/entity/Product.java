package com.hcmute.tdshop.entity;

import java.time.LocalDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.Transient;
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
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "sku", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
  private String sku;

  @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
  private String name;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

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

  @Column(name = "length")
  private Double length;

  @Column(name = "width")
  private Double width;

  @Column(name = "height")
  private Double height;

  @Column(name = "weight")
  private Double weight;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @ManyToOne
  @JoinColumn(name = "product_status", nullable = false)
  private ProductStatus status;

  @ManyToMany(mappedBy = "setOfProducts")
  private Set<Category> setOfCategories;

  @ManyToOne
  @JoinColumn(name = "brand_id")
  private Brand brand;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private Set<Image> setOfImages;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private Set<ProductAttribute> setOfProductAttributes;

  @ManyToMany
  @JoinTable(name = "product_configuration",
      joinColumns = {@JoinColumn(name = "product_id")}, inverseJoinColumns = {
      @JoinColumn(name = "variation_option_id")})
  private Set<VariationOption> setOfVariationOptions;

  @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  private Set<ProductPromotion> setOfProductPromotions;

  @Formula("(select min(pm.discount_rate) from product_promotion as pm where pm.start_date <= NOW() and pm.end_date >= NOW() and pm.product_id = id)")
  private Double currentDiscountRate;
}
