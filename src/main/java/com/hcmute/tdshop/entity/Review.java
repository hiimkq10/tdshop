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
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "rating_value", nullable = false)
  private double ratingValue;

  @Column(name = "comment", nullable = false)
  private String comment;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "is_verified")
  private boolean isVerified;

  @Column(name = "is_valid")
  private boolean isValid;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;
}
