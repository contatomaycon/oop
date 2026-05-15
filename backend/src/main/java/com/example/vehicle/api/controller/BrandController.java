package com.example.vehicle.api.controller;

import com.example.vehicle.api.dto.BrandRequest;
import com.example.vehicle.api.dto.BrandResponse;
import com.example.vehicle.application.service.BrandService;
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
@RequestMapping("/api/brands")
@Tag(name = "Brands", description = "API para gerenciamento de marcas de veículos")
@Validated
public class BrandController {
  private final BrandService brandService;
  public BrandController(BrandService brandService) { this.brandService = brandService; }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Criar nova marca", description = "Cria uma nova marca de veículo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Marca criada com sucesso",
          content = @Content(schema = @Schema(implementation = BrandResponse.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou marca já existe")
  })
  public BrandResponse create(@RequestBody @Valid BrandRequest request) { 
    return brandService.create(request); 
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Atualizar marca", description = "Atualiza os dados de uma marca")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Marca atualizada com sucesso",
          content = @Content(schema = @Schema(implementation = BrandResponse.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
      @ApiResponse(responseCode = "409", description = "Marca já existe ou não pode ser removida")
  })
  public BrandResponse update(
      @Parameter(description = "ID da marca", required = true, example = "1")
      @PathVariable @Positive Long id,
      @RequestBody @Valid BrandRequest request) {
    return brandService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Excluir marca", description = "Remove uma marca sem modelos associados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Marca removida com sucesso"),
      @ApiResponse(responseCode = "404", description = "Marca não encontrada"),
      @ApiResponse(responseCode = "409", description = "Marca possui modelos e não pode ser removida")
  })
  public void delete(
      @Parameter(description = "ID da marca", required = true, example = "1")
      @PathVariable @Positive Long id) {
    brandService.delete(id);
  }

  @GetMapping
  @Operation(summary = "Listar todas as marcas", description = "Retorna uma lista com todas as marcas cadastradas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de marcas retornada com sucesso",
          content = @Content(schema = @Schema(implementation = BrandResponse.class)))
  })
  public List<BrandResponse> list() { 
    return brandService.list(); 
  }
}
