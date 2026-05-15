import "./globals.css";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Estoque de Veículos",
  description: "Gestão de estoque de veículos",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pt-BR">
      <body>{children}</body>
    </html>
  );
}
