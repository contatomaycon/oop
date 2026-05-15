type ProblemDetail = {
  detail?: string;
  title?: string;
  error?: string;
  fields?: Array<{ field?: string; message?: string }>;
};

export function apiErrorMessage(
  body: ProblemDetail | null,
  fallback: string,
): string {
  if (!body) return fallback;
  if (Array.isArray(body.fields) && body.fields.length > 0) {
    return body.fields
      .map((field) => [field.field, field.message].filter(Boolean).join(": "))
      .join(" | ");
  }
  return body.detail || body.error || body.title || fallback;
}
