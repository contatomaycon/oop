import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { useState } from "react";
import { describe, expect, it, vi } from "vitest";
import type {
  Brand,
  Model,
  VehicleFilters as VehicleFiltersType,
} from "@/lib/types";
import VehicleFilters from "./VehicleFilters";

const brands: Brand[] = [
  { id: 1, name: "Toyota" },
  { id: 2, name: "Hyundai" },
];

const models: Model[] = [
  { id: 10, name: "Corolla", brandId: 1, brandName: "Toyota" },
  { id: 20, name: "HB20", brandId: 2, brandName: "Hyundai" },
];

function renderWithState(onApply: (draft: VehicleFiltersType) => void) {
  function Harness() {
    const [draft, setDraft] = useState<VehicleFiltersType>({});

    return (
      <VehicleFilters
        brands={brands}
        models={models}
        draft={draft}
        onDraftChange={setDraft}
        onApply={() => onApply(draft)}
        onReset={() => setDraft({})}
        onOpenCatalog={vi.fn()}
      />
    );
  }

  render(<Harness />);
}

describe("VehicleFilters", () => {
  it("filters models by selected brand and submits typed filters", async () => {
    const user = userEvent.setup();
    const onApply = vi.fn();
    renderWithState(onApply);

    expect(screen.getByLabelText("Modelo")).toBeDisabled();

    await user.selectOptions(screen.getByLabelText("Marca"), "1");

    expect(screen.getByLabelText("Modelo")).toBeEnabled();
    expect(screen.getByRole("option", { name: "Corolla" })).toBeInTheDocument();
    expect(
      screen.queryByRole("option", { name: "HB20" }),
    ).not.toBeInTheDocument();

    await user.selectOptions(screen.getByLabelText("Modelo"), "10");
    await user.type(screen.getByLabelText("Ano"), "2024");
    await user.selectOptions(screen.getByLabelText("Status"), "AVAILABLE");
    await user.type(screen.getByLabelText("Preço mín"), "50000");
    await user.type(screen.getByLabelText("Preço máx"), "90000");
    await user.click(screen.getByRole("button", { name: /filtrar/i }));

    expect(onApply).toHaveBeenCalledWith({
      brandId: 1,
      modelId: 10,
      year: 2024,
      status: "AVAILABLE",
      minPrice: 50000,
      maxPrice: 90000,
    });
  });

  it("clears selected filters", async () => {
    const user = userEvent.setup();
    renderWithState(vi.fn());

    await user.selectOptions(screen.getByLabelText("Marca"), "1");
    await user.click(screen.getByRole("button", { name: /limpar/i }));

    expect(screen.getByLabelText("Marca")).toHaveValue("");
    expect(screen.getByLabelText("Modelo")).toBeDisabled();
  });
});
