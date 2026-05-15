package com.example.vehicle.application.service;

import com.example.vehicle.api.dto.VehicleCreateRequest;
import com.example.vehicle.api.dto.VehicleResponse;
import com.example.vehicle.api.dto.VehicleUpdateRequest;
import com.example.vehicle.application.search.VehicleSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleService {
  VehicleResponse create(VehicleCreateRequest request);

  VehicleResponse get(Long id);

  VehicleResponse update(Long id, VehicleUpdateRequest request);

  void delete(Long id);

  Page<VehicleResponse> search(VehicleSearchCriteria criteria, Pageable pageable);
}
