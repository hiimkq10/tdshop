package com.hcmute.tdshop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"roles\"")
public class AccountRole {
  @Id
  @Column(name = "id", columnDefinition = "BIGINT(19)")
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(50)", unique = true, nullable = false)
  private String name;
}
