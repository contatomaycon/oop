import type { VehicleStatus } from "@/lib/types";

export const VEHICLE_STATUSES: VehicleStatus[] = [
  "AVAILABLE",
  "SOLD",
  "DISCONTINUED",
];

export const STATUS_LABEL: Record<VehicleStatus, string> = {
  AVAILABLE: "Disponível",
  SOLD: "Vendido",
  DISCONTINUED: "Descontinuado",
};

export const DEFAULT_SORT = "id,desc";
export const DEFAULT_PAGE_SIZE = 10;
