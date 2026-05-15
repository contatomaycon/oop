export type VehicleStatus = "AVAILABLE" | "SOLD" | "DISCONTINUED";

export type Brand = {
  id: number;
  name: string;
};

export type Model = {
  id: number;
  name: string;
  brandId: number;
  brandName: string;
};

export type Vehicle = {
  id: number;
  brandId: number;
  brandName: string;
  modelId: number;
  modelName: string;
  year: number;
  color: string;
  price: number;
  mileage: number;
  status: VehicleStatus;
};

export type VehicleCreateRequest = {
  modelId: number;
  year: number;
  color: string;
  price: number;
  mileage: number;
  status: VehicleStatus;
};

export type VehicleUpdateRequest = {
  modelId?: number;
  year?: number;
  color?: string;
  price?: number;
  mileage?: number;
  status?: VehicleStatus;
};

export type BrandRequest = {
  name: string;
};

export type ModelRequest = {
  name: string;
  brandId: number;
};

export type VehicleFilters = {
  brandId?: number;
  modelId?: number;
  year?: number;
  status?: VehicleStatus;
  minPrice?: number;
  maxPrice?: number;
};

export type PageResponse<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
};
