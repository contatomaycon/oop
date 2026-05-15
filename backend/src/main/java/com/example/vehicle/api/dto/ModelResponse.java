package com.example.vehicle.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta com dados de um modelo")
public record ModelResponse(
  @Schema(description = "ID do modelo", example = "1")
  Long id,
  @Schema(description = "Nome do modelo", example = "Corolla")
  String name,
  @Schema(description = "ID da marca", example = "1")
  Long brandId,
  @Schema(description = "Nome da marca", example = "Toyota")
  String brandName
) {}


