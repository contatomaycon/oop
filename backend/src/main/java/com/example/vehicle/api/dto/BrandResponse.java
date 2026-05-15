package com.example.vehicle.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta com dados de uma marca")
public record BrandResponse(
  @Schema(description = "ID da marca", example = "1")
  Long id,
  @Schema(description = "Nome da marca", example = "Toyota")
  String name
) {}


