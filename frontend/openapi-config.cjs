require('dotenv').config();

const rawBaseUrl = process.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
const serverHost = rawBaseUrl.replace(/\/api\/v1\/?$/, '');

/** @type {import('@rtk-query/codegen-openapi').ConfigFile} */
const config = {
  schemaFile: `${serverHost}/api-docs/00.%20TOTAL-API`,
  apiFile: './src/api/emptyApi.ts',
  apiImport: 'emptySplitApi',
  hooks: {
    queries: true,
    lazyQueries: true,
    mutations: true,
  },
  outputFiles: {
    './src/api/generated/fileApi.ts': {
      filterEndpoints: (_endpoint, action) => action.operation.tags?.includes('File API') ?? false,
    },
    './src/api/generated/userApi.ts': {
      filterEndpoints: (_endpoint, action) => action.operation.tags?.includes('User API') ?? false,
    },
    './src/api/generated/studentApi.ts': {
      filterEndpoints: (_endpoint, action) => action.operation.tags?.includes('Student Profile API') ?? false,
    },
    './src/api/generated/attendanceApi.ts': {
      filterEndpoints: (_endpoint, action) => action.operation.tags?.includes('Student Attendance API') ?? false,
    },
    './src/api/generated/examApi.ts': {
      filterEndpoints: (_endpoint, action) => action.operation.tags?.includes('Exam API') ?? false,
    },
  },
};

module.exports = config;
