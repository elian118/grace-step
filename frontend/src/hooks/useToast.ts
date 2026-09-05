import { useAppDispatch, useAppSelector } from '@/store';
import { addToast, removeToast, type ToastType, type ToastInstance } from '@/store/toastSlice';
import { useCallback } from 'react';
import type { CustomApiError } from '@/types/CustomApiError.ts';

export const useToast = () => {
  const dispatch = useAppDispatch();
  const toastList = useAppSelector((state) => state.toast.toastList);

  const toast = useCallback(
    (message: string, type: ToastType = 'info', duration: number = 3000) => {
      // 유니크한 고유 ID 생성 (시간 + 랜덤값 조합)
      const id = `${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
      dispatch(
        addToast({
          id,
          message,
          type,
          duration,
        }),
      );
    },
    [dispatch],
  );

  const remove = useCallback(
    (id: string) => {
      dispatch(removeToast(id));
    },
    [dispatch],
  );

  const errorHandler = (error: unknown) => {
    const errorData = error as CustomApiError;
    console.error(error);
    toast(errorData?.data?.message ?? '알 수 없는 오류가 발생했습니다.', 'error');
    throw error;
  };

  return {
    toastList,
    toast,
    remove,
    errorHandler,
  };
};

export type { ToastType, ToastInstance };
