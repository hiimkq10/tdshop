package com.hcmute.tdshop.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"addresses\"")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
  @JsonProperty("Name")
  private String name;

  @Column(name = "email", columnDefinition = "VARCHAR(100)", nullable = false)
  @Email
  @JsonProperty("Email")
  private String email;

  @JsonProperty("Phone")
  @Column(name = "phone", columnDefinition = "VARCHAR(10)", nullable = false)
  private String phone;

  @Column(name = "address_detail", columnDefinition = "VARCHAR(150)", nullable = false)
  @JsonProperty("AddressDetail")
  private String addressDetail;

  @Column(name = "is_default")
  private Boolean isDefault;

  @ManyToOne
  @JoinColumn(name = "wards_id", nullable = false)
  private Wards wards;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
