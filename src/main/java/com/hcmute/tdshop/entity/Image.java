package com.hcmute.tdshop.entity;

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
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT(19)")
  private Long id;

  @Column(name = "logo", nullable = false, unique = true)
  private String url;

  @Column(name = "label", columnDefinition = "NVARCHAR(100)")
  private String label;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;
}
