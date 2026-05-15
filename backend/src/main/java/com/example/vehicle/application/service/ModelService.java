package com.example.vehicle.application.service;

import com.example.vehicle.api.dto.ModelRequest;
import com.example.vehicle.api.dto.ModelResponse;
import java.util.List;

public interface ModelService {
  ModelResponse create(ModelRequest request);

  ModelResponse update(Long id, ModelRequest request);

  void delete(Long id);

  List<ModelResponse> listByBrand(Long brandId);

  List<ModelResponse> listAll();
}
