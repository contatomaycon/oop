package com.example.vehicle.application.service;

import com.example.vehicle.api.dto.VehicleCreateRequest;
import com.example.vehicle.api.dto.VehicleResponse;
import com.example.vehicle.api.dto.VehicleUpdateRequest;
import com.example.vehicle.api.exception.ResourceConflictException;
import com.example.vehicle.api.exception.ResourceNotFoundException;
import com.example.vehicle.application.mapper.VehicleMapper;
import com.example.vehicle.application.search.VehicleSearchCriteria;
import com.example.vehicle.application.search.VehicleSpecifications;
import com.example.vehicle.domain.model.Model;
import com.example.vehicle.domain.model.Vehicle;
import com.example.vehicle.domain.model.VehicleStatus;
import com.example.vehicle.domain.repository.ModelRepository;
import com.example.vehicle.domain.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
  private final VehicleRepository vehicleRepository;
  private final ModelRepository modelRepository;
  private final VehicleMapper vehicleMapper;

  public VehicleServiceImpl(
      VehicleRepository vehicleRepository,
      ModelRepository modelRepository,
      VehicleMapper vehicleMapper) {
    this.vehicleRepository = vehicleRepository;
    this.modelRepository = modelRepository;
    this.vehicleMapper = vehicleMapper;
  }

  @Override
  public VehicleResponse create(VehicleCreateRequest request) {
    Model model = findModel(request.modelId());
    Vehicle vehicle = new Vehicle();
    vehicle.setModel(model);
    vehicle.setYear(request.year());
    vehicle.setColor(normalizeColor(request.color()));
    vehicle.setPrice(request.price());
    vehicle.setMileage(request.mileage());
    vehicle.setStatus(request.status());
    return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
  }

  @Override
  @Transactional(readOnly = true)
  public VehicleResponse get(Long id) {
    return vehicleRepository.findWithModelBrandById(id)
        .map(vehicleMapper::toResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));
  }

  @Override
  public VehicleResponse update(Long id, VehicleUpdateRequest request) {
    Vehicle vehicle = vehicleRepository.findWithModelBrandById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

    if (request.modelId() != null) {
      vehicle.setModel(findModel(request.modelId()));
    }
    if (request.year() != null) {
      vehicle.setYear(request.year());
    }
    if (request.color() != null) {
      vehicle.setColor(normalizeColor(request.color()));
    }
    if (request.price() != null) {
      vehicle.setPrice(request.price());
    }
    if (request.mileage() != null) {
      vehicle.setMileage(request.mileage());
    }
    if (request.status() != null) {
      vehicle.setStatus(request.status());
    }

    return vehicleMapper.toResponse(vehicleRepository.save(vehicle));
  }

  @Override
  public void delete(Long id) {
    Vehicle vehicle = vehicleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));
    if (vehicle.getStatus() != VehicleStatus.SOLD && vehicle.getStatus() != VehicleStatus.DISCONTINUED) {
      throw new ResourceConflictException("Somente veículos vendidos ou descontinuados podem ser removidos");
    }
    vehicleRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<VehicleResponse> search(VehicleSearchCriteria criteria, Pageable pageable) {
    return vehicleRepository.findAll(VehicleSpecifications.byCriteria(criteria), pageable)
        .map(vehicleMapper::toResponse);
  }

  private Model findModel(Long id) {
    return modelRepository.findWithBrandById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Modelo não encontrado"));
  }

  private String normalizeColor(String value) {
    return value.trim().replaceAll("\\s+", " ");
  }
}
