import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

export type ToastType = 'info' | 'success' | 'warning' | 'error';

export interface ToastInstance {
  id: string;
  message: string;
  type?: ToastType;
  duration?: number; // 밀리초 단위, 기본 3000ms
}

interface ToastsState {
  toastList: ToastInstance[];
}

const initialState: ToastsState = {
  toastList: [],
};

export const toastSlice = createSlice({
  name: 'toast',
  initialState,
  reducers: {
    addToast: (state, action: PayloadAction<ToastInstance>) => {
      state.toastList.push(action.payload);
    },
    removeToast: (state, action: PayloadAction<string>) => {
      state.toastList = state.toastList.filter((t) => t.id !== action.payload);
    },
    clearAllToasts: (state) => {
      state.toastList = [];
    },
  },
});

export const { addToast, removeToast, clearAllToasts } = toastSlice.actions;
export default toastSlice.reducer;
