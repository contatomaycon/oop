package com.example.vehicle.api.dto;

import com.example.vehicle.domain.model.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resposta com dados de um veículo")
public record VehicleResponse(
  @Schema(description = "ID do veículo", example = "1")
  Long id,
  @Schema(description = "ID da marca", example = "1")
  Long brandId,
  @Schema(description = "Nome da marca", example = "Toyota")
  String brandName,
  @Schema(description = "ID do modelo", example = "1")
  Long modelId,
  @Schema(description = "Nome do modelo", example = "Corolla")
  String modelName,
  @Schema(description = "Ano do veículo", example = "2020")
  Integer year,
  @Schema(description = "Cor do veículo", example = "Branco")
  String color,
  @Schema(description = "Preço do veículo", example = "85000.00")
  BigDecimal price,
  @Schema(description = "Quilometragem do veículo", example = "45000")
  Long mileage,
  @Schema(description = "Status do veículo", example = "AVAILABLE")
  VehicleStatus status
) {}


