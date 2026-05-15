package com.example.vehicle.api.controller;

import com.example.vehicle.api.dto.PageResponse;
import com.example.vehicle.api.dto.VehicleCreateRequest;
import com.example.vehicle.api.dto.VehicleResponse;
import com.example.vehicle.api.dto.VehicleUpdateRequest;
import com.example.vehicle.application.search.VehiclePageRequest;
import com.example.vehicle.application.search.VehicleSearchCriteria;
import com.example.vehicle.application.service.VehicleService;
import com.example.vehicle.domain.model.VehicleStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "API para gerenciamento de veículos")
@Validated
public class VehicleController {
  private final VehicleService vehicleService;

  public VehicleController(VehicleService vehicleService) {
    this.vehicleService = vehicleService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Criar novo veículo", description = "Cria um novo veículo no estoque")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso",
          content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Modelo não encontrado")
  })
  public VehicleResponse create(@RequestBody @Valid VehicleCreateRequest request) {
    return vehicleService.create(request);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar veículo por ID", description = "Retorna os detalhes de um veículo específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Veículo encontrado",
          content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
      @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
  })
  public VehicleResponse get(
      @Parameter(description = "ID do veículo", required = true, example = "1") @PathVariable @Positive Long id) {
    return vehicleService.get(id);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Atualizar veículo", description = "Atualiza os dados de um veículo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso",
          content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
  })
  public VehicleResponse update(
      @Parameter(description = "ID do veículo", required = true, example = "1") @PathVariable @Positive Long id,
      @RequestBody @Valid VehicleUpdateRequest request) {
    return vehicleService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Excluir veículo", description = "Remove um veículo do estoque")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Veículo excluído com sucesso"),
      @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
  })
  public void delete(
      @Parameter(description = "ID do veículo", required = true, example = "1") @PathVariable @Positive Long id) {
    vehicleService.delete(id);
  }

  @GetMapping
  @Operation(summary = "Buscar veículos", description = "Busca veículos com filtros opcionais e paginação")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de veículos encontrada",
          content = @Content(schema = @Schema(implementation = PageResponse.class)))
  })
  public PageResponse<VehicleResponse> search(
      @Parameter(description = "ID da marca para filtrar", example = "1")
      @RequestParam(value = "brandId", required = false) @Positive Long brandId,
      @Parameter(description = "ID do modelo para filtrar", example = "1")
      @RequestParam(value = "modelId", required = false) @Positive Long modelId,
      @Parameter(description = "Ano do veículo para filtrar", example = "2020")
      @RequestParam(value = "year", required = false) @Min(1900) @Max(2100) Integer year,
      @Parameter(description = "Status do veículo", example = "AVAILABLE")
      @RequestParam(value = "status", required = false) VehicleStatus status,
      @Parameter(description = "Preço mínimo", example = "50000.00")
      @RequestParam(value = "minPrice", required = false) @DecimalMin("0.0") BigDecimal minPrice,
      @Parameter(description = "Preço máximo", example = "200000.00")
      @RequestParam(value = "maxPrice", required = false) @DecimalMin("0.0") BigDecimal maxPrice,
      @Parameter(description = "Número da página (inicia em 0)", example = "0")
      @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
      @Parameter(description = "Tamanho da página", example = "10")
      @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
      @Parameter(description = "Campo e direção de ordenação (ex: id,desc)", example = "id,desc")
      @RequestParam(value = "sort", defaultValue = "id,desc") String sort) {
    Pageable pageable = VehiclePageRequest.of(page, size, sort);
    VehicleSearchCriteria criteria = new VehicleSearchCriteria(brandId, modelId, year, status, minPrice, maxPrice);
    Page<VehicleResponse> vehicles = vehicleService.search(criteria, pageable);
    return PageResponse.from(vehicles);
  }
}
