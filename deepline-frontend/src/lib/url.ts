export function resolveBackendUrl(path: string) {
  if (!path) return "";

  if (/^https?:\/\//i.test(path)) return path;

  const base = import.meta.env.VITE_DEEPLINE_SERVER_URL || window.location.origin;

  return `${base}${path.startsWith("/") ? "" : "/"}${path}`;
}
