import {
  Brand,
  BrandRequest,
  Model,
  ModelRequest,
  PageResponse,
  Vehicle,
  VehicleCreateRequest,
  VehicleFilters,
  VehicleUpdateRequest,
} from "@/lib/types";
import { apiErrorMessage } from "@/lib/errors";

const getApiBase = (): string => {
  const configured =
    process.env.NEXT_PUBLIC_API_BASE_URL || process.env.API_BASE_URL || "";
  return configured.replace(/\/$/, "");
};

export const API_BASE = getApiBase();

async function apiRequest<T>(path: string, init?: RequestInit): Promise<T> {
  const url = `${API_BASE}/api${path}`;
  let res: Response;

  try {
    res = await fetch(url, {
      cache: "no-store",
      ...init,
      headers: {
        "Content-Type": "application/json",
        ...init?.headers,
      },
    });
  } catch (error) {
    if (
      error instanceof TypeError &&
      error.message.includes("Failed to fetch")
    ) {
      throw new Error(
        `Falha ao conectar na API${API_BASE ? ` em ${API_BASE}` : ""}`,
      );
    }
    throw error;
  }

  if (!res.ok) {
    const maybeJson = await res.json().catch(() => null);
    throw new Error(
      apiErrorMessage(
        maybeJson,
        `Requisição falhou: ${res.status} ${res.statusText}`,
      ),
    );
  }

  if (res.status === 204) {
    return undefined as T;
  }

  return res.json();
}

function buildQuery(
  params: Record<string, string | number | undefined>,
): string {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && `${value}` !== "") {
      query.append(key, String(value));
    }
  });
  const queryString = query.toString();
  return queryString ? `?${queryString}` : "";
}

export async function listBrands(): Promise<Brand[]> {
  return apiRequest<Brand[]>("/brands");
}

export async function createBrand(body: BrandRequest): Promise<Brand> {
  return apiRequest<Brand>("/brands", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

export async function updateBrand(
  id: number,
  body: BrandRequest,
): Promise<Brand> {
  return apiRequest<Brand>(`/brands/${id}`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
}

export async function deleteBrand(id: number): Promise<void> {
  return apiRequest<void>(`/brands/${id}`, { method: "DELETE" });
}

export async function listModels(brandId?: number): Promise<Model[]> {
  const query = buildQuery({ brandId });
  return apiRequest<Model[]>(`/models${query}`);
}

export async function createModel(body: ModelRequest): Promise<Model> {
  return apiRequest<Model>("/models", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

export async function updateModel(
  id: number,
  body: ModelRequest,
): Promise<Model> {
  return apiRequest<Model>(`/models/${id}`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
}

export async function deleteModel(id: number): Promise<void> {
  return apiRequest<void>(`/models/${id}`, { method: "DELETE" });
}

export async function searchVehicles(
  filters: VehicleFilters,
  page: number,
  size: number,
  sort: string,
): Promise<PageResponse<Vehicle>> {
  const query = buildQuery({ ...filters, page, size, sort });
  return apiRequest<PageResponse<Vehicle>>(`/vehicles${query}`);
}

export async function createVehicle(
  body: VehicleCreateRequest,
): Promise<Vehicle> {
  return apiRequest<Vehicle>("/vehicles", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

export async function updateVehicle(
  id: number,
  body: VehicleUpdateRequest,
): Promise<Vehicle> {
  return apiRequest<Vehicle>(`/vehicles/${id}`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
}

export async function deleteVehicle(id: number): Promise<void> {
  return apiRequest<void>(`/vehicles/${id}`, { method: "DELETE" });
}
