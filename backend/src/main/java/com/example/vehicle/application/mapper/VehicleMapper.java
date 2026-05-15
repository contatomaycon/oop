package com.example.vehicle.application.mapper;

import com.example.vehicle.api.dto.VehicleResponse;
import com.example.vehicle.domain.model.Brand;
import com.example.vehicle.domain.model.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
  public VehicleResponse toResponse(Vehicle vehicle) {
    Brand brand = vehicle.getModel().getBrand();
    return new VehicleResponse(
        vehicle.getId(),
        brand.getId(),
        brand.getName(),
        vehicle.getModel().getId(),
        vehicle.getModel().getName(),
        vehicle.getYear(),
        vehicle.getColor(),
        vehicle.getPrice(),
        vehicle.getMileage(),
        vehicle.getStatus());
  }
}
