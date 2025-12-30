import axios, { type AxiosInstance, type CreateAxiosDefaults } from "axios";

export interface ApiConfig {
  serverUrl: string;
  defaultUrl: string;
  timeout?: number;
  headers?: Record<string, string>;
}

export const createApiClient = (config: ApiConfig): AxiosInstance => {
  const axiosConfig: CreateAxiosDefaults = {
    baseURL: `${config.serverUrl || config.defaultUrl}/api/v1`,
    timeout: config.timeout || 5000,
    headers: {
      "Content-Type": "application/json",
      ...config.headers,
    },
  };

  const instance = axios.create(axiosConfig);

  instance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem("accessToken");

      if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  instance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response && error.response.status === 401) {
        alert("세션이 만료되었습니다. 다시 로그인해주세요.");
        localStorage.removeItem("accessToken");
        window.location.href = "/login";
      }

      return Promise.reject(error);
    }
  );

  return instance;
};
