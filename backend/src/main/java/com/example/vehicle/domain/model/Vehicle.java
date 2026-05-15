package com.example.vehicle.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "vehicle")
public class Vehicle {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "model_id")
  private Model model;

  @Column(name = "year", nullable = false)
  private Integer year;

  @Column(name = "color", nullable = false, length = 60)
  private String color;

  @Column(name = "price", nullable = false, precision = 12, scale = 2)
  private BigDecimal price;

  @Column(name = "mileage", nullable = false)
  private Long mileage;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private VehicleStatus status;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  void prePersist() {
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  void preUpdate() { this.updatedAt = Instant.now(); }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Model getModel() { return model; }
  public void setModel(Model model) { this.model = model; }
  public Integer getYear() { return year; }
  public void setYear(Integer year) { this.year = year; }
  public String getColor() { return color; }
  public void setColor(String color) { this.color = color; }
  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }
  public Long getMileage() { return mileage; }
  public void setMileage(Long mileage) { this.mileage = mileage; }
  public VehicleStatus getStatus() { return status; }
  public void setStatus(VehicleStatus status) { this.status = status; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}


