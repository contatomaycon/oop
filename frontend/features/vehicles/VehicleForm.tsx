"use client";

import { Save, Tags, X } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { STATUS_LABEL, VEHICLE_STATUSES } from "@/lib/constants";
import {
  Brand,
  Model,
  Vehicle,
  VehicleCreateRequest,
  VehicleStatus,
  VehicleUpdateRequest,
} from "@/lib/types";

type Props = {
  open: boolean;
  mode: "create" | "edit";
  brands: Brand[];
  models: Model[];
  vehicle?: Vehicle | null;
  onClose: () => void;
  onSubmit: (
    payload: VehicleCreateRequest | VehicleUpdateRequest,
  ) => Promise<void>;
  onOpenCatalog: () => void;
  loading?: boolean;
};

type FormState = {
  brandId?: number;
  modelId?: number;
  year: string;
  color: string;
  price: string;
  mileage: string;
  status: VehicleStatus;
};

const defaultState: FormState = {
  brandId: undefined,
  modelId: undefined,
  year: "",
  color: "",
  price: "",
  mileage: "",
  status: "AVAILABLE",
};

function deriveInitialState(
  vehicle: Vehicle | null | undefined,
  models: Model[],
): FormState {
  if (!vehicle) return defaultState;
  const model = models.find((m) => m.id === vehicle.modelId);
  return {
    brandId: model?.brandId ?? vehicle.brandId,
    modelId: vehicle.modelId,
    year: String(vehicle.year),
    color: vehicle.color,
    price: String(vehicle.price),
    mileage: String(vehicle.mileage),
    status: vehicle.status,
  };
}

export default function VehicleForm({
  open,
  mode,
  brands,
  models,
  vehicle,
  onClose,
  onSubmit,
  onOpenCatalog,
  loading,
}: Props) {
  const [form, setForm] = useState<FormState>(defaultState);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    if (!open) return;
    setForm(deriveInitialState(vehicle, models));
    setError("");
  }, [open, vehicle, models]);

  const modelsForBrand = useMemo(() => {
    if (!form.brandId) return [];
    return models.filter((m) => m.brandId === form.brandId);
  }, [form.brandId, models]);

  if (!open) return null;

  const submit = async () => {
    if (!form.modelId) {
      setError("Selecione o modelo");
      return;
    }

    const year = Number(form.year);
    const price = Number(form.price);
    const mileage = Number(form.mileage);

    if (!year || year < 1900 || year > 2100) {
      setError("Ano deve estar entre 1900 e 2100");
      return;
    }
    if (form.color.trim().length === 0 || form.color.trim().length > 60) {
      setError("Cor obrigatória e com no máximo 60 caracteres");
      return;
    }
    if (
      !Number.isFinite(price) ||
      price < 0 ||
      !/^\d+(\.\d{1,2})?$/.test(form.price)
    ) {
      setError(
        "Preço deve ser maior ou igual a 0 e ter no máximo 2 casas decimais",
      );
      return;
    }
    if (!Number.isFinite(mileage) || mileage < 0) {
      setError("Quilometragem deve ser maior ou igual a 0");
      return;
    }

    setError("");

    const payload = {
      modelId: form.modelId,
      year,
      color: form.color.trim(),
      price,
      mileage,
      status: form.status,
    };

    await onSubmit(payload);
  };

  return (
    <div
      className="modal-backdrop"
      role="dialog"
      aria-modal="true"
      aria-labelledby="vehicle-form-title"
    >
      <div className="modal-card modal-large">
        <h3 id="vehicle-form-title">
          {mode === "create"
            ? "Novo veículo"
            : `Editar veículo #${vehicle?.id}`}
        </h3>
        <div className="form-grid">
          <div>
            <label htmlFor="vehicle-form-brand">Marca</label>
            <select
              id="vehicle-form-brand"
              value={form.brandId ?? ""}
              onChange={(e) =>
                setForm((prev) => ({
                  ...prev,
                  brandId: e.target.value ? Number(e.target.value) : undefined,
                  modelId: undefined,
                }))
              }
            >
              <option value="">Selecione</option>
              {brands.map((b) => (
                <option key={b.id} value={b.id}>
                  {b.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label htmlFor="vehicle-form-model">Modelo</label>
            <select
              id="vehicle-form-model"
              value={form.modelId ?? ""}
              disabled={!form.brandId}
              onChange={(e) =>
                setForm((prev) => ({
                  ...prev,
                  modelId: e.target.value ? Number(e.target.value) : undefined,
                }))
              }
            >
              <option value="">Selecione</option>
              {modelsForBrand.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label htmlFor="vehicle-form-year">Ano</label>
            <input
              id="vehicle-form-year"
              type="number"
              value={form.year}
              onChange={(e) =>
                setForm((prev) => ({ ...prev, year: e.target.value }))
              }
            />
          </div>
          <div>
            <label htmlFor="vehicle-form-color">Cor</label>
            <input
              id="vehicle-form-color"
              value={form.color}
              onChange={(e) =>
                setForm((prev) => ({ ...prev, color: e.target.value }))
              }
            />
          </div>
          <div>
            <label htmlFor="vehicle-form-price">Preço</label>
            <input
              id="vehicle-form-price"
              type="number"
              step="0.01"
              value={form.price}
              onChange={(e) =>
                setForm((prev) => ({ ...prev, price: e.target.value }))
              }
            />
          </div>
          <div>
            <label htmlFor="vehicle-form-mileage">Quilometragem</label>
            <input
              id="vehicle-form-mileage"
              type="number"
              value={form.mileage}
              onChange={(e) =>
                setForm((prev) => ({ ...prev, mileage: e.target.value }))
              }
            />
          </div>
          <div>
            <label htmlFor="vehicle-form-status">Status</label>
            <select
              id="vehicle-form-status"
              value={form.status}
              onChange={(e) =>
                setForm((prev) => ({
                  ...prev,
                  status: e.target.value as VehicleStatus,
                }))
              }
            >
              {VEHICLE_STATUSES.map((status) => (
                <option key={status} value={status}>
                  {STATUS_LABEL[status]}
                </option>
              ))}
            </select>
          </div>
        </div>
        {error ? <p className="error-text">{error}</p> : null}
        <div className="modal-actions">
          <button
            className="btn-secondary"
            onClick={onOpenCatalog}
            disabled={loading}
          >
            <Tags size={17} aria-hidden />
            Catálogo
          </button>
          <button
            className="btn-secondary"
            onClick={onClose}
            disabled={loading}
          >
            <X size={17} aria-hidden />
            Cancelar
          </button>
          <button onClick={submit} disabled={loading}>
            <Save size={17} aria-hidden />
            {mode === "create" ? "Criar" : "Salvar"}
          </button>
        </div>
      </div>
    </div>
  );
}
