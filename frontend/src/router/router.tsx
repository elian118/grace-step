import Layout from '@/components/layout/Layout';
import { createBrowserRouter, Navigate } from 'react-router-dom';
import { menus } from '@/router/menus.tsx';
import ErrorPage from '@/components/error/ErrorPage.tsx';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      // / 접속 시 home 상대 경로로 이동
      { index: true, element: <Navigate to="home" replace /> },
      ...menus.map((menu) => ({
        path: menu.path,
        element: menu.element,
      })),
    ],
    errorElement: <ErrorPage />,
  },
]);
