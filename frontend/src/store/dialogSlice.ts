import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

export interface DialogOptions {
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl' | '3xl' | '4xl' | '5xl' | '6xl' | '7xl' | 'full';
  preventCloseOnEsc?: boolean;
  preventCloseOnOutsideClick?: boolean;
  blurBackground?: boolean;
  allowDrag?: boolean;
}

export interface DialogInstance {
  key: string;
  title?: string;
  props?: never;
  options?: DialogOptions;
}

interface DialogsState {
  activeDialogs: DialogInstance[];
}

const initialState: DialogsState = {
  activeDialogs: [],
};

export const dialogSlice = createSlice({
  name: 'dialog',
  initialState,
  reducers: {
    openDialog: (state, action: PayloadAction<DialogInstance>) => {
      const index = state.activeDialogs.findIndex((d) => d.key === action.payload.key);
      if (index !== -1) {
        state.activeDialogs[index] = action.payload;
      } else {
        state.activeDialogs.push(action.payload);
      }
    },
    closeDialog: (state, action: PayloadAction<string>) => {
      state.activeDialogs = state.activeDialogs.filter((d) => d.key !== action.payload);
    },
    closeAllDialogs: (state) => {
      state.activeDialogs = [];
    },
  },
});

export const { openDialog, closeDialog, closeAllDialogs } = dialogSlice.actions;
export default dialogSlice.reducer;
