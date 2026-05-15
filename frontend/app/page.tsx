"use client";

import { Plus, Tags } from "lucide-react";
import { useCallback, useEffect, useState } from "react";
import {
  createVehicle,
  deleteVehicle,
  listBrands,
  listModels,
  searchVehicles,
  updateVehicle,
} from "@/lib/api";
import { DEFAULT_PAGE_SIZE, DEFAULT_SORT } from "@/lib/constants";
import type {
  Brand,
  Model,
  Vehicle,
  VehicleCreateRequest,
  VehicleFilters as VehicleFiltersType,
  VehicleUpdateRequest,
} from "@/lib/types";
import ConfirmDialog from "@/components/shared/ConfirmDialog";
import BrandModelManagerModal from "@/features/catalog/BrandModelManagerModal";
import VehicleFilters from "@/features/vehicles/VehicleFilters";
import VehicleForm from "@/features/vehicles/VehicleForm";
import VehiclesTable from "@/features/vehicles/VehiclesTable";

const defaultFilters: VehicleFiltersType = {};

export default function Page() {
  const [filtersDraft, setFiltersDraft] =
    useState<VehicleFiltersType>(defaultFilters);
  const [filtersApplied, setFiltersApplied] =
    useState<VehicleFiltersType>(defaultFilters);
  const [brands, setBrands] = useState<Brand[]>([]);
  const [models, setModels] = useState<Model[]>([]);
  const [data, setData] = useState<Vehicle[]>([]);
  const [page, setPage] = useState(0);
  const [size] = useState(DEFAULT_PAGE_SIZE);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [sort, setSort] = useState(DEFAULT_SORT);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [vehicleModalOpen, setVehicleModalOpen] = useState(false);
  const [vehicleModalMode, setVehicleModalMode] = useState<"create" | "edit">(
    "create",
  );
  const [selectedVehicle, setSelectedVehicle] = useState<Vehicle | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [catalogModalOpen, setCatalogModalOpen] = useState(false);
  const [mutationLoading, setMutationLoading] = useState(false);

  const loadCatalog = useCallback(async () => {
    const [loadedBrands, loadedModels] = await Promise.all([
      listBrands(),
      listModels(),
    ]);
    setBrands(loadedBrands);
    setModels(loadedModels);
  }, []);

  const fetchData = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const res = await searchVehicles(filtersApplied, page, size, sort);
      setData(res.content);
      setTotalElements(res.totalElements);
      setTotalPages(res.totalPages);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Falha ao buscar veiculos");
    } finally {
      setLoading(false);
    }
  }, [filtersApplied, page, size, sort]);

  useEffect(() => {
    const load = async () => {
      try {
        await loadCatalog();
      } catch (err) {
        setError(
          err instanceof Error
            ? err.message
            : "Falha ao carregar marcas/modelos",
        );
      }
    };
    load();
  }, [loadCatalog]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const openCreateModal = () => {
    setSelectedVehicle(null);
    setVehicleModalMode("create");
    setVehicleModalOpen(true);
  };

  const openEditModal = (vehicle: Vehicle) => {
    setSelectedVehicle(vehicle);
    setVehicleModalMode("edit");
    setVehicleModalOpen(true);
  };

  const runVehicleSubmit = async (
    payload: VehicleCreateRequest | VehicleUpdateRequest,
  ) => {
    setMutationLoading(true);
    setError("");
    try {
      if (vehicleModalMode === "create") {
        await createVehicle(payload as VehicleCreateRequest);
      } else if (selectedVehicle) {
        await updateVehicle(
          selectedVehicle.id,
          payload as VehicleUpdateRequest,
        );
      }
      setVehicleModalOpen(false);
      setSelectedVehicle(null);
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Falha ao salvar veiculo");
    } finally {
      setMutationLoading(false);
    }
  };

  const runDelete = async () => {
    if (!selectedVehicle) return;
    setMutationLoading(true);
    setError("");
    try {
      await deleteVehicle(selectedVehicle.id);
      setDeleteDialogOpen(false);
      setSelectedVehicle(null);
      await fetchData();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Falha ao excluir veiculo");
    } finally {
      setMutationLoading(false);
    }
  };

  return (
    <main className="page-wrap">
      <section className="topbar">
        <div className="title-block">
          <p className="eyebrow">Sistema Automotivo</p>
          <h1>Estoque de Veículos</h1>
        </div>
        <div className="topbar-actions">
          <button
            className="btn-secondary"
            onClick={() => setCatalogModalOpen(true)}
          >
            <Tags size={18} aria-hidden />
            Marcas e modelos
          </button>
          <button onClick={openCreateModal}>
            <Plus size={18} aria-hidden />
            Novo veículo
          </button>
        </div>
      </section>

      <section className="toolbar">
        <VehicleFilters
          brands={brands}
          models={models}
          draft={filtersDraft}
          onDraftChange={setFiltersDraft}
          onApply={() => {
            setPage(0);
            setFiltersApplied(filtersDraft);
          }}
          onReset={() => {
            setFiltersDraft(defaultFilters);
            setFiltersApplied(defaultFilters);
            setPage(0);
          }}
          onOpenCatalog={() => setCatalogModalOpen(true)}
        />
      </section>

      {error ? <p className="error-text">{error}</p> : null}

      <section className="data-section">
        <div className="section-header">
          <h2>Veículos</h2>
          <p className="result-counter">Total de registros: {totalElements}</p>
        </div>

        {loading ? (
          <p className="loading-text">Carregando...</p>
        ) : (
          <>
            <div className="table-wrap">
              <VehiclesTable
                data={data}
                sort={sort}
                onSortChange={(nextSort) => {
                  setPage(0);
                  setSort(nextSort);
                }}
                onEdit={openEditModal}
                onDelete={(vehicle) => {
                  setSelectedVehicle(vehicle);
                  setDeleteDialogOpen(true);
                }}
              />
            </div>
            <div className="pagination">
              <button
                className="btn-secondary"
                onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                disabled={page === 0}
              >
                Anterior
              </button>
              <span>
                Página {page + 1} de {Math.max(totalPages, 1)}
              </span>
              <button
                className="btn-secondary"
                onClick={() => setPage((prev) => prev + 1)}
                disabled={page + 1 >= totalPages}
              >
                Proxima
              </button>
            </div>
          </>
        )}
      </section>

      <VehicleForm
        open={vehicleModalOpen}
        mode={vehicleModalMode}
        brands={brands}
        models={models}
        vehicle={selectedVehicle}
        onClose={() => {
          setVehicleModalOpen(false);
          setSelectedVehicle(null);
        }}
        onSubmit={runVehicleSubmit}
        onOpenCatalog={() => setCatalogModalOpen(true)}
        loading={mutationLoading}
      />

      <ConfirmDialog
        open={deleteDialogOpen}
        title="Remover veículo"
        message={`Deseja remover o veículo #${selectedVehicle?.id}? Apenas vendidos ou descontinuados podem ser removidos.`}
        confirmLabel="Excluir"
        onConfirm={runDelete}
        onCancel={() => {
          setDeleteDialogOpen(false);
          setSelectedVehicle(null);
        }}
        loading={mutationLoading}
      />

      <BrandModelManagerModal
        open={catalogModalOpen}
        brands={brands}
        models={models}
        onClose={() => setCatalogModalOpen(false)}
        onChanged={async () => {
          await loadCatalog();
          await fetchData();
        }}
      />
    </main>
  );
}
