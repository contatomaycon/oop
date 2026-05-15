package com.example.vehicle.api.dto;

import com.example.vehicle.domain.model.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Dados para atualização de um veículo")
public record VehicleUpdateRequest(
  @Schema(description = "ID do modelo do veículo", example = "1")
  @Positive Long modelId,
  @Schema(description = "Novo ano do veículo (1900-2100)", example = "2021")
  @Min(1900) @Max(2100) Integer year,
  @Schema(description = "Nova cor do veículo", example = "Preto")
  @Size(max = 60) String color,
  @Schema(description = "Novo preço do veículo", example = "90000.00")
  @DecimalMin("0.0") @Digits(integer = 10, fraction = 2) BigDecimal price,
  @Schema(description = "Nova quilometragem do veículo", example = "50000")
  @Min(0) Long mileage,
  @Schema(description = "Novo status do veículo", example = "SOLD")
  VehicleStatus status
) {}

