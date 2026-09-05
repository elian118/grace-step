export type CustomApiError = {
  data?: {
    code?: string;
    message?: string;
    status?: number;
    errors?: Array<unknown>;
  };
  status?: number;
};
