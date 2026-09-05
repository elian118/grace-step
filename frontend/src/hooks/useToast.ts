import { useAppDispatch, useAppSelector } from '@/store';
import { addToast, removeToast, type ToastType, type ToastInstance } from '@/store/toastSlice';
import { useCallback } from 'react';

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

  return {
    toastList,
    toast,
    remove,
  };
};

export type { ToastType, ToastInstance };
