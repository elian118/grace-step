import Layout from '@/components/layout/Layout';
import { createBrowserRouter } from 'react-router-dom';
import { menus } from '@/router/menus.tsx';
import Error from '@/components/error/Error.tsx';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: menus.map((menu) => ({ path: menu.path, element: menu.element })),
    errorElement: <Error />,
  },
]);
