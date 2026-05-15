package com.example.vehicle.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "model", uniqueConstraints = {@UniqueConstraint(name = "uq_model_brand_name", columnNames = {"brand_id","name"})})
public class Model {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String name;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id")
  private Brand brand;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public Brand getBrand() { return brand; }
  public void setBrand(Brand brand) { this.brand = brand; }
}


