package com.example.vehicle.application.mapper;

import com.example.vehicle.api.dto.ModelResponse;
import com.example.vehicle.domain.model.Model;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {
  public ModelResponse toResponse(Model model) {
    return new ModelResponse(
        model.getId(),
        model.getName(),
        model.getBrand().getId(),
        model.getBrand().getName());
  }
}
