"use client";

import { Trash2, X } from "lucide-react";

type Props = {
  open: boolean;
  title: string;
  message: string;
  confirmLabel?: string;
  onConfirm: () => void | Promise<void>;
  onCancel: () => void;
  loading?: boolean;
};

export default function ConfirmDialog({
  open,
  title,
  message,
  confirmLabel = "Confirmar",
  onConfirm,
  onCancel,
  loading,
}: Props) {
  if (!open) return null;

  return (
    <div
      className="modal-backdrop"
      role="dialog"
      aria-modal="true"
      aria-labelledby="confirm-dialog-title"
    >
      <div className="modal-card">
        <h3 id="confirm-dialog-title">{title}</h3>
        <p>{message}</p>
        <div className="modal-actions">
          <button
            className="btn-secondary"
            onClick={onCancel}
            disabled={loading}
          >
            <X size={17} aria-hidden />
            Cancelar
          </button>
          <button className="btn-danger" onClick={onConfirm} disabled={loading}>
            <Trash2 size={17} aria-hidden />
            {confirmLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
