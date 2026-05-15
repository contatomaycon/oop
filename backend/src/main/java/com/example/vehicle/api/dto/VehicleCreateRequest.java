package com.example.vehicle.api.dto;

import com.example.vehicle.domain.model.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Dados para criação de um novo veículo")
public record VehicleCreateRequest(
    @Schema(description = "ID do modelo do veículo", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull @Positive Long modelId,
    @Schema(description = "Ano do veículo (1900-2100)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2020")
    @NotNull @Min(1900) @Max(2100) Integer year,
    @Schema(description = "Cor do veículo", requiredMode = Schema.RequiredMode.REQUIRED, example = "Branco")
    @NotBlank @Size(max = 60) String color,
    @Schema(description = "Preço do veículo", requiredMode = Schema.RequiredMode.REQUIRED, example = "85000.00")
    @NotNull @DecimalMin("0.0") @Digits(integer = 10, fraction = 2) BigDecimal price,
    @Schema(description = "Quilometragem do veículo", requiredMode = Schema.RequiredMode.REQUIRED, example = "45000")
    @NotNull @Min(0) Long mileage,
    @Schema(description = "Status do veículo", requiredMode = Schema.RequiredMode.REQUIRED, example = "AVAILABLE")
    @NotNull VehicleStatus status
) {}
