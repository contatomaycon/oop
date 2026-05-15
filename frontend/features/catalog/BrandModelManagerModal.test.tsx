import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { createBrand, deleteBrand } from "@/lib/api";
import type { Brand, Model } from "@/lib/types";
import BrandModelManagerModal from "./BrandModelManagerModal";

vi.mock("@/lib/api", () => ({
  createBrand: vi.fn(),
  createModel: vi.fn(),
  deleteBrand: vi.fn(),
  deleteModel: vi.fn(),
  updateBrand: vi.fn(),
  updateModel: vi.fn(),
}));

const brands: Brand[] = [{ id: 1, name: "Toyota" }];
const models: Model[] = [
  { id: 10, name: "Corolla", brandId: 1, brandName: "Toyota" },
];

describe("BrandModelManagerModal", () => {
  it("uses the custom confirmation dialog before deleting a brand", async () => {
    const user = userEvent.setup();
    const onChanged = vi.fn().mockResolvedValue(undefined);
    vi.mocked(deleteBrand).mockResolvedValue(undefined);

    render(
      <BrandModelManagerModal
        open
        brands={brands}
        models={models}
        onClose={vi.fn()}
        onChanged={onChanged}
      />,
    );

    await user.click(screen.getByRole("button", { name: /excluir/i }));

    const dialog = screen.getByRole("dialog", { name: "Excluir registro" });
    expect(
      within(dialog).getByText("Deseja excluir Toyota?"),
    ).toBeInTheDocument();

    await user.click(within(dialog).getByRole("button", { name: /cancelar/i }));
    expect(deleteBrand).not.toHaveBeenCalled();

    await user.click(screen.getByRole("button", { name: /excluir/i }));
    const confirmDialog = screen.getByRole("dialog", {
      name: "Excluir registro",
    });
    await user.click(
      within(confirmDialog).getByRole("button", { name: /excluir/i }),
    );

    await waitFor(() => {
      expect(deleteBrand).toHaveBeenCalledWith(1);
      expect(onChanged).toHaveBeenCalled();
    });
  });

  it("shows ProblemDetail errors returned by catalog API actions", async () => {
    const user = userEvent.setup();
    vi.mocked(createBrand).mockRejectedValue(new Error("name: já existe"));

    render(
      <BrandModelManagerModal
        open
        brands={brands}
        models={models}
        onClose={vi.fn()}
        onChanged={vi.fn()}
      />,
    );

    await user.type(screen.getByPlaceholderText("Nome da marca"), "Toyota");
    await user.click(screen.getByRole("button", { name: /criar/i }));

    expect(await screen.findByText("name: já existe")).toBeInTheDocument();
  });
});
