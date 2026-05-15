package com.example.vehicle.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.vehicle.application.mapper.VehicleMapper;
import com.example.vehicle.domain.model.Brand;
import com.example.vehicle.domain.model.Model;
import com.example.vehicle.domain.model.Vehicle;
import com.example.vehicle.domain.model.VehicleStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class VehicleMapperTest {
  private final VehicleMapper mapper = new VehicleMapper();

  @Test
  void mapsVehicleWithModelAndBrand() {
    Brand brand = new Brand();
    brand.setId(1L);
    brand.setName("Honda");

    Model model = new Model();
    model.setId(2L);
    model.setName("Civic");
    model.setBrand(brand);

    Vehicle vehicle = new Vehicle();
    vehicle.setId(3L);
    vehicle.setModel(model);
    vehicle.setYear(2022);
    vehicle.setColor("Branco");
    vehicle.setPrice(new BigDecimal("110000.00"));
    vehicle.setMileage(12000L);
    vehicle.setStatus(VehicleStatus.AVAILABLE);

    var response = mapper.toResponse(vehicle);

    assertThat(response.id()).isEqualTo(3L);
    assertThat(response.brandName()).isEqualTo("Honda");
    assertThat(response.modelName()).isEqualTo("Civic");
    assertThat(response.price()).isEqualByComparingTo("110000.00");
    assertThat(response.status()).isEqualTo(VehicleStatus.AVAILABLE);
  }
}
