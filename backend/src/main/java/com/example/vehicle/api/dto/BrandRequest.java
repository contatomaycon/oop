package com.example.vehicle.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de uma nova marca")
public record BrandRequest(
    @Schema(description = "Nome da marca", requiredMode = Schema.RequiredMode.REQUIRED, example = "Toyota")
    @NotBlank @Size(max = 120) String name
) {}
