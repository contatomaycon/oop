package com.example.vehicle.domain.repository;

import com.example.vehicle.domain.model.Vehicle;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
  @EntityGraph(attributePaths = {"model", "model.brand"})
  Optional<Vehicle> findWithModelBrandById(Long id);

  boolean existsByModelId(Long modelId);
}

