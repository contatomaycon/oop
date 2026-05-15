package com.example.vehicle.api.controller;

import com.example.vehicle.api.dto.ModelRequest;
import com.example.vehicle.api.dto.ModelResponse;
import com.example.vehicle.application.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/models")
@Tag(name = "Models", description = "API para gerenciamento de modelos de veículos")
@Validated
public class ModelController {
  private final ModelService modelService;
  public ModelController(ModelService modelService) { this.modelService = modelService; }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Criar novo modelo", description = "Cria um novo modelo de veículo associado a uma marca")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Modelo criado com sucesso",
          content = @Content(schema = @Schema(implementation = ModelResponse.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou modelo já existe para a marca"),
      @ApiResponse(responseCode = "404", description = "Marca não encontrada")
  })
  public ModelResponse create(@RequestBody @Valid ModelRequest request) { 
    return modelService.create(request); 
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Atualizar modelo", description = "Atualiza os dados de um modelo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Modelo atualizado com sucesso",
          content = @Content(schema = @Schema(implementation = ModelResponse.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Modelo ou marca não encontrado"),
      @ApiResponse(responseCode = "409", description = "Modelo já existe para a marca")
  })
  public ModelResponse update(
      @Parameter(description = "ID do modelo", required = true, example = "1")
      @PathVariable @Positive Long id,
      @RequestBody @Valid ModelRequest request) {
    return modelService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Excluir modelo", description = "Remove um modelo sem veículos associados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Modelo removido com sucesso"),
      @ApiResponse(responseCode = "404", description = "Modelo não encontrado"),
      @ApiResponse(responseCode = "409", description = "Modelo possui veículos e não pode ser removido")
  })
  public void delete(
      @Parameter(description = "ID do modelo", required = true, example = "1")
      @PathVariable @Positive Long id) {
    modelService.delete(id);
  }

  @GetMapping
  @Operation(summary = "Listar modelos", description = "Retorna lista de modelos. Pode filtrar por marca usando o parâmetro brandId")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de modelos retornada com sucesso",
          content = @Content(schema = @Schema(implementation = ModelResponse.class)))
  })
  public List<ModelResponse> list(
      @Parameter(description = "ID da marca para filtrar modelos (opcional)", example = "1")
      @RequestParam(value = "brandId", required = false) @Positive Long brandId) {
    if (brandId != null) return modelService.listByBrand(brandId);
    return modelService.listAll();
  }
}
