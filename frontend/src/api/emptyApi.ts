import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const emptySplitApi = createApi({
  baseQuery: fetchBaseQuery({
    baseUrl: '/',
    prepareHeaders: (headers) => {
      // 공통 헤더 추가
      headers.set('X-User-Id', import.meta.env.VITE_ADMIN_ID);
      return headers;
    },
  }),
  endpoints: () => ({}),
});
