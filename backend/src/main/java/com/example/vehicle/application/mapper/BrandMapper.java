package com.example.vehicle.application.mapper;

import com.example.vehicle.api.dto.BrandResponse;
import com.example.vehicle.domain.model.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
  public BrandResponse toResponse(Brand brand) {
    return new BrandResponse(brand.getId(), brand.getName());
  }
}
