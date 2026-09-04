import './App.css';
import { RouterProvider } from 'react-router-dom';
import { router } from '@/router/router.tsx';
import { Provider } from 'react-redux';
import { store } from '@/store';
import { DialogContainer } from '@/components/common/dialog/DialogContainer';
import { ToastContainer } from '@/components/common/toast/ToastContainer';

function App() {
  return (
    <Provider store={store}>
      <RouterProvider router={router} />
      <DialogContainer />
      <ToastContainer />
    </Provider>
  );
}

export default App;
