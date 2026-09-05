import { configureStore } from '@reduxjs/toolkit';
import { type TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import { emptySplitApi } from '@/api/emptyApi';
import dialogReducer from './dialogSlice';
import toastReducer from './toastSlice';

export const store = configureStore({
  reducer: {
    [emptySplitApi.reducerPath]: emptySplitApi.reducer,
    dialog: dialogReducer,
    toast: toastReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        // 다이얼로그 props로 콜백 함수나 복잡한 객체(비직렬화 가능 데이터)가 전달될 수 있으므로,
        // 이로 인한 직렬화 경고를 방지하기 위해 예외 처리를 추가합니다.
        ignoredActions: ['dialog/openDialog'],
        ignoredPaths: ['dialog.activeDialogs'],
      },
    }).concat(emptySplitApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch: () => AppDispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
