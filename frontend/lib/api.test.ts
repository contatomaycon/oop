import { beforeEach, describe, expect, it, vi } from "vitest";

const fetchMock = vi.fn();

function jsonResponse(
  body: unknown,
  status = 200,
  statusText = "OK",
): Response {
  return {
    ok: status >= 200 && status < 300,
    status,
    statusText,
    json: vi.fn().mockResolvedValue(body),
  } as unknown as Response;
}

describe("api client", () => {
  beforeEach(() => {
    vi.resetModules();
    vi.stubGlobal("fetch", fetchMock);
    fetchMock.mockReset();
    delete process.env.NEXT_PUBLIC_API_BASE_URL;
    delete process.env.API_BASE_URL;
  });

  it("builds vehicle search queries against the Next.js API rewrite by default", async () => {
    fetchMock.mockResolvedValue(
      jsonResponse({
        content: [],
        totalElements: 0,
        totalPages: 0,
        number: 1,
        size: 20,
      }),
    );

    const { searchVehicles } = await import("./api");

    await searchVehicles(
      {
        brandId: 2,
        modelId: 5,
        year: 2024,
        status: "AVAILABLE",
        minPrice: 50000,
        maxPrice: 90000,
      },
      1,
      20,
      "price,asc",
    );

    expect(fetchMock).toHaveBeenCalledWith(
      "/api/vehicles?brandId=2&modelId=5&year=2024&status=AVAILABLE&minPrice=50000&maxPrice=90000&page=1&size=20&sort=price%2Casc",
      expect.objectContaining({
        cache: "no-store",
        headers: { "Content-Type": "application/json" },
      }),
    );
  });

  it("honors NEXT_PUBLIC_API_BASE_URL when configured", async () => {
    process.env.NEXT_PUBLIC_API_BASE_URL = "http://localhost:8080/";
    fetchMock.mockResolvedValue(jsonResponse([]));

    const { listBrands } = await import("./api");

    await listBrands();

    expect(fetchMock).toHaveBeenCalledWith(
      "http://localhost:8080/api/brands",
      expect.any(Object),
    );
  });

  it("formats ProblemDetail field validation errors", async () => {
    fetchMock.mockResolvedValue(
      jsonResponse(
        {
          title: "Dados inválidos",
          fields: [{ field: "name", message: "não deve estar em branco" }],
        },
        400,
        "Bad Request",
      ),
    );

    const { createBrand } = await import("./api");

    await expect(createBrand({ name: "" })).rejects.toThrow(
      "name: não deve estar em branco",
    );
  });
});
