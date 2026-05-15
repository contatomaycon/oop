package com.example.vehicle.domain.repository;

import com.example.vehicle.domain.model.Model;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
  List<Model> findByBrandId(Long brandId);

  @EntityGraph(attributePaths = "brand")
  List<Model> findByBrandIdOrderByNameAsc(Long brandId);

  @EntityGraph(attributePaths = "brand")
  List<Model> findAllByOrderByBrandNameAscNameAsc();

  @EntityGraph(attributePaths = "brand")
  Optional<Model> findWithBrandById(Long id);

  boolean existsByBrandId(Long brandId);
  boolean existsByBrandIdAndNameIgnoreCase(Long brandId, String name);
  boolean existsByBrandIdAndNameIgnoreCaseAndIdNot(Long brandId, String name, Long id);
}
