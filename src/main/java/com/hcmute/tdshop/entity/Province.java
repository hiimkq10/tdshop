package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Province {
  @Id
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(100)")
  @JsonProperty("Name")
  private String name;

  @Column(name = "short_name", columnDefinition = "VARCHAR(100)")
  @JsonProperty("ShortName")
  private String shortName;

  @Column(name = "type", columnDefinition = "VARCHAR(100)")
  @JsonProperty("Type")
  private String type;

  @Column(name = "type_priority")
  @JsonProperty("TypePriority")
  private int typePriority;

  @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<District> setOfDistricts;
}
