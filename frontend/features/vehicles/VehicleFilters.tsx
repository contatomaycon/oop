"use client";

import { RotateCcw, Search, Tags } from "lucide-react";
import { STATUS_LABEL, VEHICLE_STATUSES } from "@/lib/constants";
import type {
  Brand,
  Model,
  VehicleFilters as VehicleFiltersType,
  VehicleStatus,
} from "@/lib/types";

type Props = {
  brands: Brand[];
  models: Model[];
  draft: VehicleFiltersType;
  onDraftChange: (next: VehicleFiltersType) => void;
  onApply: () => void;
  onReset: () => void;
  onOpenCatalog: () => void;
};

export default function VehicleFilters({
  brands,
  models,
  draft,
  onDraftChange,
  onApply,
  onReset,
  onOpenCatalog,
}: Props) {
  const currentBrandId = draft.brandId;
  const filteredModels = currentBrandId
    ? models.filter((m) => m.brandId === currentBrandId)
    : [];

  return (
    <div className="filters-grid">
      <div>
        <label htmlFor="vehicle-filter-brand">Marca</label>
        <select
          id="vehicle-filter-brand"
          value={draft.brandId ?? ""}
          onChange={(e) => {
            const brandId = e.target.value ? Number(e.target.value) : undefined;
            onDraftChange({ ...draft, brandId, modelId: undefined });
          }}
        >
          <option value="">Todas</option>
          {brands.map((b) => (
            <option key={b.id} value={b.id}>
              {b.name}
            </option>
          ))}
        </select>
      </div>
      <div>
        <label htmlFor="vehicle-filter-model">Modelo</label>
        <select
          id="vehicle-filter-model"
          value={draft.modelId ?? ""}
          onChange={(e) =>
            onDraftChange({
              ...draft,
              modelId: e.target.value ? Number(e.target.value) : undefined,
            })
          }
          disabled={!currentBrandId}
        >
          <option value="">Todos</option>
          {filteredModels.map((m) => (
            <option key={m.id} value={m.id}>
              {m.name}
            </option>
          ))}
        </select>
      </div>
      <div>
        <label htmlFor="vehicle-filter-year">Ano</label>
        <input
          id="vehicle-filter-year"
          type="number"
          value={draft.year ?? ""}
          onChange={(e) =>
            onDraftChange({
              ...draft,
              year: e.target.value ? Number(e.target.value) : undefined,
            })
          }
          placeholder="Ex: 2022"
        />
      </div>
      <div>
        <label htmlFor="vehicle-filter-status">Status</label>
        <select
          id="vehicle-filter-status"
          value={draft.status ?? ""}
          onChange={(e) =>
            onDraftChange({
              ...draft,
              status: (e.target.value || undefined) as
                | VehicleStatus
                | undefined,
            })
          }
        >
          <option value="">Todos</option>
          {VEHICLE_STATUSES.map((status) => (
            <option key={status} value={status}>
              {STATUS_LABEL[status]}
            </option>
          ))}
        </select>
      </div>
      <div>
        <label htmlFor="vehicle-filter-min-price">Preço mín</label>
        <input
          id="vehicle-filter-min-price"
          type="number"
          value={draft.minPrice ?? ""}
          onChange={(e) =>
            onDraftChange({
              ...draft,
              minPrice: e.target.value ? Number(e.target.value) : undefined,
            })
          }
          placeholder="0"
        />
      </div>
      <div>
        <label htmlFor="vehicle-filter-max-price">Preço máx</label>
        <input
          id="vehicle-filter-max-price"
          type="number"
          value={draft.maxPrice ?? ""}
          onChange={(e) =>
            onDraftChange({
              ...draft,
              maxPrice: e.target.value ? Number(e.target.value) : undefined,
            })
          }
          placeholder="100000"
        />
      </div>
      <div className="filters-actions">
        <button onClick={onApply}>
          <Search size={17} aria-hidden />
          Filtrar
        </button>
        <button className="btn-secondary" onClick={onReset}>
          <RotateCcw size={17} aria-hidden />
          Limpar
        </button>
        <button className="btn-secondary" onClick={onOpenCatalog}>
          <Tags size={17} aria-hidden />
          Catálogo
        </button>
      </div>
    </div>
  );
}
