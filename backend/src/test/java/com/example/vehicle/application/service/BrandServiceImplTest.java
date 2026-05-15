package com.example.vehicle.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.vehicle.api.dto.BrandRequest;
import com.example.vehicle.api.exception.ResourceConflictException;
import com.example.vehicle.application.mapper.BrandMapper;
import com.example.vehicle.domain.model.Brand;
import com.example.vehicle.domain.repository.BrandRepository;
import com.example.vehicle.domain.repository.ModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {
  @Mock
  private BrandRepository brandRepository;

  @Mock
  private ModelRepository modelRepository;

  @Spy
  private BrandMapper brandMapper = new BrandMapper();

  @InjectMocks
  private BrandServiceImpl brandService;

  @Test
  void createNormalizesNameBeforeSaving() {
    Brand saved = new Brand();
    saved.setId(10L);
    saved.setName("Toyota Motors");
    when(brandRepository.save(org.mockito.ArgumentMatchers.any(Brand.class))).thenReturn(saved);

    var response = brandService.create(new BrandRequest("  Toyota   Motors  "));

    ArgumentCaptor<Brand> captor = ArgumentCaptor.forClass(Brand.class);
    verify(brandRepository).save(captor.capture());
    assertThat(captor.getValue().getName()).isEqualTo("Toyota Motors");
    assertThat(response.id()).isEqualTo(10L);
    assertThat(response.name()).isEqualTo("Toyota Motors");
  }

  @Test
  void createRejectsDuplicateName() {
    when(brandRepository.existsByNameIgnoreCase("Toyota")).thenReturn(true);

    assertThatThrownBy(() -> brandService.create(new BrandRequest("Toyota")))
        .isInstanceOf(ResourceConflictException.class)
        .hasMessageContaining("Marca já existe");
  }
}
