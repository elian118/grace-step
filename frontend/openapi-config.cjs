/** @type {import('@rtk-query/codegen-openapi').ConfigFile} */
const config = {
  schemaFile: 'http://localhost:8080/api-docs/00.%20TOTAL-API',
  apiFile: './src/api/emptyApi.ts',
  apiImport: 'emptySplitApi',
  hooks: {
    queries: true,
    lazyQueries: true,
    mutations: true,
  },
  outputFiles: {
    './src/api/generated/userApi.ts': {
      filterEndpoints: ['User API', /^\/api\/v1\/users/],
    },
    './src/api/generated/studentApi.ts': {
      filterEndpoints: ['Student Profile API', 'Student Attendance API', /^\/api\/v1\/students/],
    },
    './src/api/generated/examApi.ts': {
      filterEndpoints: ['Exam API', /^\/api\/v1\/classes\/exams/],
    },
  },
};

module.exports = config;
