package com.example.vehicle.application.search;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class VehiclePageRequest {
  private static final Map<String, String> SORT_FIELDS = Map.ofEntries(
      Map.entry("id", "id"),
      Map.entry("brandName", "model.brand.name"),
      Map.entry("model.brand.name", "model.brand.name"),
      Map.entry("modelName", "model.name"),
      Map.entry("model.name", "model.name"),
      Map.entry("year", "year"),
      Map.entry("price", "price"),
      Map.entry("mileage", "mileage"),
      Map.entry("status", "status"));

  private VehiclePageRequest() {
  }

  public static Pageable of(int page, int size, String sort) {
    String[] parts = (sort == null || sort.isBlank() ? "id,desc" : sort).split(",", 2);
    String property = SORT_FIELDS.get(parts[0].trim());
    if (property == null) {
      throw new IllegalArgumentException("Campo de ordenação não suportado: " + parts[0]);
    }
    Sort.Direction direction = parts.length > 1
        ? Sort.Direction.fromString(parts[1].trim())
        : Sort.Direction.DESC;
    return PageRequest.of(page, size, Sort.by(direction, property));
  }
}
