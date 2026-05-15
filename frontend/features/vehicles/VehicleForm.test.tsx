import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import type { Brand, Model } from "@/lib/types";
import VehicleForm from "./VehicleForm";

const brands: Brand[] = [{ id: 1, name: "Toyota" }];
const models: Model[] = [
  { id: 10, name: "Corolla", brandId: 1, brandName: "Toyota" },
];

describe("VehicleForm", () => {
  it("validates required model before submit", async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn();

    render(
      <VehicleForm
        open
        mode="create"
        brands={brands}
        models={models}
        onClose={vi.fn()}
        onSubmit={onSubmit}
        onOpenCatalog={vi.fn()}
      />,
    );

    await user.click(screen.getByRole("button", { name: /criar/i }));

    expect(screen.getByText("Selecione o modelo")).toBeInTheDocument();
    expect(onSubmit).not.toHaveBeenCalled();
  });

  it("submits a normalized vehicle payload", async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn().mockResolvedValue(undefined);

    render(
      <VehicleForm
        open
        mode="create"
        brands={brands}
        models={models}
        onClose={vi.fn()}
        onSubmit={onSubmit}
        onOpenCatalog={vi.fn()}
      />,
    );

    await user.selectOptions(screen.getByLabelText("Marca"), "1");
    await user.selectOptions(screen.getByLabelText("Modelo"), "10");
    await user.type(screen.getByLabelText("Ano"), "2024");
    await user.type(screen.getByLabelText("Cor"), "  Prata  ");
    await user.type(screen.getByLabelText("Preço"), "105000.50");
    await user.type(screen.getByLabelText("Quilometragem"), "12000");
    await user.selectOptions(screen.getByLabelText("Status"), "SOLD");
    await user.click(screen.getByRole("button", { name: /criar/i }));

    await waitFor(() => {
      expect(onSubmit).toHaveBeenCalledWith({
        modelId: 10,
        year: 2024,
        color: "Prata",
        price: 105000.5,
        mileage: 12000,
        status: "SOLD",
      });
    });
  });

  it("rejects prices with more than two decimal places", async () => {
    const user = userEvent.setup();

    render(
      <VehicleForm
        open
        mode="create"
        brands={brands}
        models={models}
        onClose={vi.fn()}
        onSubmit={vi.fn()}
        onOpenCatalog={vi.fn()}
      />,
    );

    await user.selectOptions(screen.getByLabelText("Marca"), "1");
    await user.selectOptions(screen.getByLabelText("Modelo"), "10");
    await user.type(screen.getByLabelText("Ano"), "2024");
    await user.type(screen.getByLabelText("Cor"), "Prata");
    await user.type(screen.getByLabelText("Preço"), "105000.555");
    await user.type(screen.getByLabelText("Quilometragem"), "12000");
    await user.click(screen.getByRole("button", { name: /criar/i }));

    expect(
      screen.getByText(
        "Preço deve ser maior ou igual a 0 e ter no máximo 2 casas decimais",
      ),
    ).toBeInTheDocument();
  });
});
