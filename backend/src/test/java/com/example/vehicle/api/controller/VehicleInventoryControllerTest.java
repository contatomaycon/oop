package com.example.vehicle.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.vehicle.api.dto.BrandRequest;
import com.example.vehicle.api.dto.BrandResponse;
import com.example.vehicle.api.dto.ModelRequest;
import com.example.vehicle.api.dto.ModelResponse;
import com.example.vehicle.api.dto.VehicleCreateRequest;
import com.example.vehicle.api.dto.VehicleResponse;
import com.example.vehicle.api.dto.VehicleUpdateRequest;
import com.example.vehicle.domain.model.VehicleStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(properties = {
    "debug=false",
    "logging.level.org.springframework=INFO",
    "logging.level.org.hibernate.SQL=INFO",
    "springdoc.api-docs.enabled=false",
    "springdoc.swagger-ui.enabled=false"
})
@AutoConfigureMockMvc
@Testcontainers
class VehicleInventoryControllerTest {
  @Container
  static final MySQLContainer<?> MYSQL = new MySQLContainer<>(DockerImageName.parse("mysql:8.4"))
      .withDatabaseName("vehicledb")
      .withUsername("vehuser")
      .withPassword("vehpass");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DynamicPropertySource
  static void databaseProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
    registry.add("spring.datasource.username", MYSQL::getUsername);
    registry.add("spring.datasource.password", MYSQL::getPassword);
  }

  @Test
  void managesCatalogAndVehicleCrudFlow() throws Exception {
    BrandResponse brand = createBrand("Test Brand " + System.nanoTime());
    ModelResponse model = createModel("Model One", brand.id());

    VehicleResponse vehicle = createVehicle(new VehicleCreateRequest(
        model.id(),
        2024,
        "Branco",
        new BigDecimal("120000.00"),
        8000L,
        VehicleStatus.AVAILABLE));

    JsonNode filtered = objectMapper.readTree(mockMvc.perform(get("/api/vehicles")
            .param("brandId", brand.id().toString())
            .param("modelId", model.id().toString())
            .param("year", "2024")
            .param("status", "AVAILABLE")
            .param("minPrice", "100000")
            .param("maxPrice", "130000")
            .param("sort", "price,asc"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString());

    assertThat(filtered.get("totalElements").asLong()).isGreaterThanOrEqualTo(1);
    assertThat(filtered.get("content"))
        .anySatisfy(node -> assertThat(node.get("id").asLong()).isEqualTo(vehicle.id()));

    VehicleResponse updated = objectMapper.readValue(mockMvc.perform(patch("/api/vehicles/{id}", vehicle.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new VehicleUpdateRequest(
                null,
                null,
                null,
                new BigDecimal("118500.00"),
                9000L,
                VehicleStatus.SOLD))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price").value(118500.00))
        .andExpect(jsonPath("$.mileage").value(9000))
        .andExpect(jsonPath("$.status").value("SOLD"))
        .andReturn().getResponse().getContentAsString(), VehicleResponse.class);

    mockMvc.perform(delete("/api/vehicles/{id}", updated.id()))
        .andExpect(status().isNoContent());
  }

  @Test
  void rejectsDeletingAvailableVehicleAndDuplicateBrands() throws Exception {
    BrandResponse brand = createBrand("Duplicate Test " + System.nanoTime());
    ModelResponse model = createModel("Model Duplicate", brand.id());
    VehicleResponse vehicle = createVehicle(new VehicleCreateRequest(
        model.id(),
        2023,
        "Prata",
        new BigDecimal("90000.00"),
        10000L,
        VehicleStatus.AVAILABLE));

    mockMvc.perform(delete("/api/vehicles/{id}", vehicle.id()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.type").value("urn:problem:conflict"));

    mockMvc.perform(post("/api/brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new BrandRequest(brand.name().toLowerCase()))))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.type").value("urn:problem:conflict"));
  }

  @Test
  void rejectsInvalidSearchParameters() throws Exception {
    mockMvc.perform(get("/api/vehicles")
            .param("minPrice", "500")
            .param("maxPrice", "100")
            .param("sort", "unknown,asc"))
        .andExpect(status().isBadRequest());
  }

  private BrandResponse createBrand(String name) throws Exception {
    String body = mockMvc.perform(post("/api/brands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new BrandRequest(name))))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    return objectMapper.readValue(body, BrandResponse.class);
  }

  private ModelResponse createModel(String name, Long brandId) throws Exception {
    String body = mockMvc.perform(post("/api/models")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new ModelRequest(name, brandId))))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    return objectMapper.readValue(body, ModelResponse.class);
  }

  private VehicleResponse createVehicle(VehicleCreateRequest request) throws Exception {
    String body = mockMvc.perform(post("/api/vehicles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    return objectMapper.readValue(body, VehicleResponse.class);
  }
}
