package com.example.vehicle.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo modelo")
public record ModelRequest(
    @Schema(description = "Nome do modelo", requiredMode = Schema.RequiredMode.REQUIRED, example = "Corolla")
    @NotBlank @Size(max = 120) String name,
    @Schema(description = "ID da marca do modelo", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull @Positive Long brandId
) {}
