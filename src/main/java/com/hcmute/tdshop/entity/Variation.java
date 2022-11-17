package com.hcmute.tdshop.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class Variation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT(19)")
  private Long id;

  @Column(name = "name", columnDefinition = "NVARCHAR(100)", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "master_category_id")
  private MasterCategory masterCategory;

  @OneToMany(mappedBy = "variation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<VariationOption> setOfVariationOptions;
}
