import React from 'react';
import { useAppSelector } from '@/store';
import { ToastItem } from './ToastItem';

export const ToastContainer: React.FC = () => {
  const toastList = useAppSelector((state) => state.toast.toastList);

  if (toastList.length === 0) return null;

  return (
    // 'toast-start' 와 'toast-bottom' 에 의해 화면 왼쪽 아래에 배치되며
    // DaisyUI 5의 toast 레이아웃이 적용되어 토스트 아이템들이 수직으로(아래에서 위로) 정렬됩니다.
    <div className="toast toast-start toast-bottom p-6 gap-2 z-9999 pointer-events-none">
      <div className="flex flex-col gap-2 pointer-events-auto">
        {toastList.map((item) => (
          <ToastItem key={item.id} item={item} />
        ))}
      </div>
    </div>
  );
};
