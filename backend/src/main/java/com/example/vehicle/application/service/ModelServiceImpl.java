package com.example.vehicle.application.service;

import com.example.vehicle.api.dto.ModelRequest;
import com.example.vehicle.api.dto.ModelResponse;
import com.example.vehicle.api.exception.ResourceConflictException;
import com.example.vehicle.api.exception.ResourceNotFoundException;
import com.example.vehicle.application.mapper.ModelMapper;
import com.example.vehicle.domain.model.Brand;
import com.example.vehicle.domain.model.Model;
import com.example.vehicle.domain.repository.BrandRepository;
import com.example.vehicle.domain.repository.ModelRepository;
import com.example.vehicle.domain.repository.VehicleRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModelServiceImpl implements ModelService {
  private final ModelRepository modelRepository;
  private final BrandRepository brandRepository;
  private final VehicleRepository vehicleRepository;
  private final ModelMapper modelMapper;

  public ModelServiceImpl(
      ModelRepository modelRepository,
      BrandRepository brandRepository,
      VehicleRepository vehicleRepository,
      ModelMapper modelMapper) {
    this.modelRepository = modelRepository;
    this.brandRepository = brandRepository;
    this.vehicleRepository = vehicleRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public ModelResponse create(ModelRequest request) {
    Brand brand = findBrand(request.brandId());
    String normalizedName = normalizeName(request.name());
    if (modelRepository.existsByBrandIdAndNameIgnoreCase(brand.getId(), normalizedName)) {
      throw new ResourceConflictException("Modelo já existe para esta marca");
    }

    Model model = new Model();
    model.setName(normalizedName);
    model.setBrand(brand);
    return modelMapper.toResponse(modelRepository.save(model));
  }

  @Override
  public ModelResponse update(Long id, ModelRequest request) {
    Model model = modelRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Modelo não encontrado"));
    Brand brand = findBrand(request.brandId());
    String normalizedName = normalizeName(request.name());
    if (modelRepository.existsByBrandIdAndNameIgnoreCaseAndIdNot(brand.getId(), normalizedName, id)) {
      throw new ResourceConflictException("Modelo já existe para esta marca");
    }

    model.setName(normalizedName);
    model.setBrand(brand);
    return modelMapper.toResponse(modelRepository.save(model));
  }

  @Override
  public void delete(Long id) {
    Model model = modelRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Modelo não encontrado"));
    if (vehicleRepository.existsByModelId(model.getId())) {
      throw new ResourceConflictException("Modelo possui veículos e não pode ser removido");
    }
    modelRepository.deleteById(model.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ModelResponse> listByBrand(Long brandId) {
    if (!brandRepository.existsById(brandId)) {
      throw new ResourceNotFoundException("Marca não encontrada");
    }
    return modelRepository.findByBrandIdOrderByNameAsc(brandId).stream()
        .map(modelMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ModelResponse> listAll() {
    return modelRepository.findAllByOrderByBrandNameAscNameAsc().stream()
        .map(modelMapper::toResponse)
        .toList();
  }

  private Brand findBrand(Long id) {
    return brandRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
  }

  private String normalizeName(String value) {
    return value.trim().replaceAll("\\s+", " ");
  }
}
