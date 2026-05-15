"use client";

import { Pencil, Plus, RotateCcw, Save, Trash2, X } from "lucide-react";
import { useState } from "react";
import ConfirmDialog from "@/components/shared/ConfirmDialog";
import {
  createBrand,
  createModel,
  deleteBrand,
  deleteModel,
  updateBrand,
  updateModel,
} from "@/lib/api";
import { Brand, Model } from "@/lib/types";

type Props = {
  open: boolean;
  brands: Brand[];
  models: Model[];
  onClose: () => void;
  onChanged: () => Promise<void>;
};

export default function BrandModelManagerModal({
  open,
  brands,
  models,
  onClose,
  onChanged,
}: Props) {
  const [tab, setTab] = useState<"brands" | "models">("brands");
  const [brandName, setBrandName] = useState("");
  const [editingBrandId, setEditingBrandId] = useState<number | null>(null);
  const [modelName, setModelName] = useState("");
  const [modelBrandId, setModelBrandId] = useState<number | undefined>(
    undefined,
  );
  const [editingModelId, setEditingModelId] = useState<number | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [pendingDelete, setPendingDelete] = useState<
    | { type: "brand"; id: number; name: string }
    | { type: "model"; id: number; name: string }
    | null
  >(null);

  if (!open) return null;

  const submitBrand = async () => {
    if (!brandName.trim()) return;
    setLoading(true);
    setError("");
    try {
      if (editingBrandId) {
        await updateBrand(editingBrandId, { name: brandName });
      } else {
        await createBrand({ name: brandName });
      }
      setBrandName("");
      setEditingBrandId(null);
      await onChanged();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Falha ao salvar marca");
    } finally {
      setLoading(false);
    }
  };

  const submitModel = async () => {
    if (!modelName.trim() || !modelBrandId) return;
    setLoading(true);
    setError("");
    try {
      if (editingModelId) {
        await updateModel(editingModelId, {
          name: modelName,
          brandId: modelBrandId,
        });
      } else {
        await createModel({ name: modelName, brandId: modelBrandId });
      }
      setModelName("");
      setModelBrandId(undefined);
      setEditingModelId(null);
      await onChanged();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Falha ao salvar modelo");
    } finally {
      setLoading(false);
    }
  };

  const removePending = async () => {
    if (!pendingDelete) return;
    setLoading(true);
    setError("");
    try {
      if (pendingDelete.type === "brand") {
        await deleteBrand(pendingDelete.id);
      } else {
        await deleteModel(pendingDelete.id);
      }
      setPendingDelete(null);
      await onChanged();
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "Falha ao excluir registro",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="modal-backdrop"
      role="dialog"
      aria-modal="true"
      aria-labelledby="catalog-title"
    >
      <div className="modal-card modal-large">
        <h3 id="catalog-title">Marcas e modelos</h3>
        <div className="catalog-tabs">
          <button
            className={tab === "brands" ? "tab-active" : "btn-secondary"}
            onClick={() => setTab("brands")}
          >
            Marcas
          </button>
          <button
            className={tab === "models" ? "tab-active" : "btn-secondary"}
            onClick={() => setTab("models")}
          >
            Modelos
          </button>
        </div>

        {tab === "brands" ? (
          <div>
            <div className="inline-form">
              <input
                placeholder="Nome da marca"
                value={brandName}
                onChange={(e) => setBrandName(e.target.value)}
              />
              <button onClick={submitBrand} disabled={loading}>
                {editingBrandId ? (
                  <Save size={17} aria-hidden />
                ) : (
                  <Plus size={17} aria-hidden />
                )}
                {editingBrandId ? "Salvar" : "Criar"}
              </button>
              <button
                className="btn-secondary"
                onClick={() => {
                  setBrandName("");
                  setEditingBrandId(null);
                }}
                disabled={loading}
              >
                <RotateCcw size={17} aria-hidden />
                Limpar
              </button>
            </div>
            <ul className="catalog-list">
              {brands.map((brand) => (
                <li key={brand.id}>
                  <span>{brand.name}</span>
                  <div>
                    <button
                      className="btn-secondary"
                      onClick={() => {
                        setEditingBrandId(brand.id);
                        setBrandName(brand.name);
                      }}
                    >
                      <Pencil size={16} aria-hidden />
                      Editar
                    </button>
                    <button
                      className="btn-danger"
                      onClick={() =>
                        setPendingDelete({
                          type: "brand",
                          id: brand.id,
                          name: brand.name,
                        })
                      }
                    >
                      <Trash2 size={16} aria-hidden />
                      Excluir
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        ) : (
          <div>
            <div className="inline-form">
              <select
                value={modelBrandId ?? ""}
                onChange={(e) =>
                  setModelBrandId(
                    e.target.value ? Number(e.target.value) : undefined,
                  )
                }
              >
                <option value="">Marca do modelo</option>
                {brands.map((b) => (
                  <option key={b.id} value={b.id}>
                    {b.name}
                  </option>
                ))}
              </select>
              <input
                placeholder="Nome do modelo"
                value={modelName}
                onChange={(e) => setModelName(e.target.value)}
              />
              <button onClick={submitModel} disabled={loading}>
                {editingModelId ? (
                  <Save size={17} aria-hidden />
                ) : (
                  <Plus size={17} aria-hidden />
                )}
                {editingModelId ? "Salvar" : "Criar"}
              </button>
              <button
                className="btn-secondary"
                onClick={() => {
                  setModelName("");
                  setModelBrandId(undefined);
                  setEditingModelId(null);
                }}
                disabled={loading}
              >
                <RotateCcw size={17} aria-hidden />
                Limpar
              </button>
            </div>
            <ul className="catalog-list">
              {models.map((model) => (
                <li key={model.id}>
                  <span>
                    {model.name} ({model.brandName})
                  </span>
                  <div>
                    <button
                      className="btn-secondary"
                      onClick={() => {
                        setEditingModelId(model.id);
                        setModelBrandId(model.brandId);
                        setModelName(model.name);
                      }}
                    >
                      <Pencil size={16} aria-hidden />
                      Editar
                    </button>
                    <button
                      className="btn-danger"
                      onClick={() =>
                        setPendingDelete({
                          type: "model",
                          id: model.id,
                          name: model.name,
                        })
                      }
                    >
                      <Trash2 size={16} aria-hidden />
                      Excluir
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        )}

        {error ? <p className="error-text">{error}</p> : null}

        <div className="modal-actions">
          <button
            className="btn-secondary"
            onClick={onClose}
            disabled={loading}
          >
            <X size={17} aria-hidden />
            Fechar
          </button>
        </div>
      </div>

      <ConfirmDialog
        open={pendingDelete !== null}
        title="Excluir registro"
        message={`Deseja excluir ${pendingDelete?.name}?`}
        confirmLabel="Excluir"
        onConfirm={removePending}
        onCancel={() => setPendingDelete(null)}
        loading={loading}
      />
    </div>
  );
}
