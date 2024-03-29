package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Wards {

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

  @ManyToOne
  @JoinColumn(name = "district_id", nullable = false)
  @JsonIgnore
  private District district;
}
