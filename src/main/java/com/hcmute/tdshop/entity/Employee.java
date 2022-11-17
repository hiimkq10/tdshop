package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT(19)")
  @JsonProperty("Id")
  private Long id;

  @MapsId
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id")
  @JsonProperty("UserInfo")
  private User userInfo;

  @Column(name = "salary")
  private long salary;
}
