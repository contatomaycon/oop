"use client";

import { Pencil, Trash2 } from "lucide-react";
import { STATUS_LABEL } from "@/lib/constants";
import { formatCurrency, formatMileage } from "@/lib/format";
import { Vehicle } from "@/lib/types";

type SortField =
  | "id"
  | "brandName"
  | "modelName"
  | "year"
  | "price"
  | "mileage"
  | "status";

type Props = {
  data: Vehicle[];
  sort: string;
  onSortChange: (sort: string) => void;
  onEdit: (vehicle: Vehicle) => void;
  onDelete: (vehicle: Vehicle) => void;
};

const sortFieldToApi: Record<SortField, string> = {
  id: "id",
  brandName: "model.brand.name",
  modelName: "model.name",
  year: "year",
  price: "price",
  mileage: "mileage",
  status: "status",
};

function SortableHeader({
  label,
  field,
  sort,
  onSortChange,
}: {
  label: string;
  field: SortField;
  sort: string;
  onSortChange: (sort: string) => void;
}) {
  const [activeField, direction = "desc"] = sort.split(",");
  const apiField = sortFieldToApi[field];
  const isActive = activeField === apiField;

  return (
    <th>
      <button
        className="th-button"
        onClick={() => {
          if (!isActive) {
            onSortChange(`${apiField},asc`);
            return;
          }
          onSortChange(`${apiField},${direction === "asc" ? "desc" : "asc"}`);
        }}
      >
        {label}
        {isActive ? (direction === "asc" ? " ▲" : " ▼") : ""}
      </button>
    </th>
  );
}

export default function VehiclesTable({
  data,
  sort,
  onSortChange,
  onEdit,
  onDelete,
}: Props) {
  if (!data.length) {
    return <p className="empty-state">Nenhum veículo encontrado.</p>;
  }

  return (
    <table className="vehicles-table">
      <thead>
        <tr>
          <SortableHeader
            label="ID"
            field="id"
            sort={sort}
            onSortChange={onSortChange}
          />
          <SortableHeader
            label="Marca"
            field="brandName"
            sort={sort}
            onSortChange={onSortChange}
          />
          <SortableHeader
            label="Modelo"
            field="modelName"
            sort={sort}
            onSortChange={onSortChange}
          />
          <SortableHeader
            label="Ano"
            field="year"
            sort={sort}
            onSortChange={onSortChange}
          />
          <th>Cor</th>
          <SortableHeader
            label="Preço"
            field="price"
            sort={sort}
            onSortChange={onSortChange}
          />
          <SortableHeader
            label="KM"
            field="mileage"
            sort={sort}
            onSortChange={onSortChange}
          />
          <SortableHeader
            label="Status"
            field="status"
            sort={sort}
            onSortChange={onSortChange}
          />
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        {data.map((v) => (
          <tr key={v.id}>
            <td>{v.id}</td>
            <td>{v.brandName}</td>
            <td>{v.modelName}</td>
            <td>{v.year}</td>
            <td>{v.color}</td>
            <td>{formatCurrency(v.price)}</td>
            <td>{formatMileage(v.mileage)}</td>
            <td>
              <span className={`status-pill status-${v.status.toLowerCase()}`}>
                {STATUS_LABEL[v.status]}
              </span>
            </td>
            <td className="actions-cell">
              <button
                className="btn-icon btn-secondary"
                onClick={() => onEdit(v)}
                aria-label={`Editar veículo ${v.id}`}
              >
                <Pencil size={16} aria-hidden />
              </button>
              <button
                className="btn-icon btn-danger"
                onClick={() => onDelete(v)}
                aria-label={`Excluir veículo ${v.id}`}
              >
                <Trash2 size={16} aria-hidden />
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
