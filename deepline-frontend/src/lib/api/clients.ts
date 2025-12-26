import { createApiClient } from "@/lib/api/createApiClient.ts";

export const deeplineApi = createApiClient({
  serverUrl: import.meta.env.VITE_DEEPLINE_SERVER_URL,

  defaultUrl: "",
});
