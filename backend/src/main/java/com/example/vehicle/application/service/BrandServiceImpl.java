package com.example.vehicle.application.service;

import com.example.vehicle.api.dto.BrandRequest;
import com.example.vehicle.api.dto.BrandResponse;
import com.example.vehicle.api.exception.ResourceConflictException;
import com.example.vehicle.api.exception.ResourceNotFoundException;
import com.example.vehicle.application.mapper.BrandMapper;
import com.example.vehicle.domain.model.Brand;
import com.example.vehicle.domain.repository.BrandRepository;
import com.example.vehicle.domain.repository.ModelRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
  private final BrandRepository brandRepository;
  private final ModelRepository modelRepository;
  private final BrandMapper brandMapper;

  public BrandServiceImpl(
      BrandRepository brandRepository,
      ModelRepository modelRepository,
      BrandMapper brandMapper) {
    this.brandRepository = brandRepository;
    this.modelRepository = modelRepository;
    this.brandMapper = brandMapper;
  }

  @Override
  public BrandResponse create(BrandRequest request) {
    String normalizedName = normalizeName(request.name());
    if (brandRepository.existsByNameIgnoreCase(normalizedName)) {
      throw new ResourceConflictException("Marca já existe");
    }

    Brand brand = new Brand();
    brand.setName(normalizedName);
    return brandMapper.toResponse(brandRepository.save(brand));
  }

  @Override
  public BrandResponse update(Long id, BrandRequest request) {
    Brand brand = brandRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
    String normalizedName = normalizeName(request.name());
    if (brandRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
      throw new ResourceConflictException("Marca já existe");
    }

    brand.setName(normalizedName);
    return brandMapper.toResponse(brandRepository.save(brand));
  }

  @Override
  public void delete(Long id) {
    Brand brand = brandRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada"));
    if (modelRepository.existsByBrandId(brand.getId())) {
      throw new ResourceConflictException("Marca possui modelos e não pode ser removida");
    }
    brandRepository.deleteById(brand.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public List<BrandResponse> list() {
    return brandRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
        .map(brandMapper::toResponse)
        .toList();
  }

  private String normalizeName(String value) {
    return value.trim().replaceAll("\\s+", " ");
  }
}
