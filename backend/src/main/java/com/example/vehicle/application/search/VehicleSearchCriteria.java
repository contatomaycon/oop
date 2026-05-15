package com.example.vehicle.application.search;

import com.example.vehicle.domain.model.VehicleStatus;
import java.math.BigDecimal;

public record VehicleSearchCriteria(
    Long brandId,
    Long modelId,
    Integer year,
    VehicleStatus status,
    BigDecimal minPrice,
    BigDecimal maxPrice) {

  public VehicleSearchCriteria {
    if (minPrice != null && minPrice.signum() < 0) {
      throw new IllegalArgumentException("minPrice deve ser maior ou igual a zero");
    }
    if (maxPrice != null && maxPrice.signum() < 0) {
      throw new IllegalArgumentException("maxPrice deve ser maior ou igual a zero");
    }
    if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
      throw new IllegalArgumentException("minPrice não pode ser maior que maxPrice");
    }
  }
}
