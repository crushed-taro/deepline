import path from "path"
import tailwindcss from "@tailwindcss/vite"
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/api': {
        target: process.env.VITE_DEEPLINE_SERVER_URL || "http://localhost:8080",
        changeOrigin: true,
        secure: false,
      },
      '/ws-chat': {
        target: process.env.VITE_DEEPLINE_SERVER_URL || 'http://localhost:8080',
        ws: true,
      }
    },
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  define: {
    global: "window",
  }
})
