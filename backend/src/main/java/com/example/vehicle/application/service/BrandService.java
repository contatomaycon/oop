package com.example.vehicle.application.service;

import com.example.vehicle.api.dto.BrandRequest;
import com.example.vehicle.api.dto.BrandResponse;
import java.util.List;

public interface BrandService {
  BrandResponse create(BrandRequest request);

  BrandResponse update(Long id, BrandRequest request);

  void delete(Long id);

  List<BrandResponse> list();
}
