import React, { useEffect } from 'react';
import type { ToastInstance } from '@/store/toastSlice';
import { useToast } from '@/hooks/useToast';
import { CheckCircleIcon, InfoIcon, WarningIcon, XCircleIcon } from '@phosphor-icons/react';
import styles from './styles/ToastItem.module.css';

interface ToastItemProps {
  item: ToastInstance;
}

const TYPE_CONFIG = {
  info: {
    alertClass: 'alert-info text-info-content',
    icon: <InfoIcon size={20} className="shrink-0" />,
  },
  success: {
    alertClass: 'alert-success text-success-content',
    icon: <CheckCircleIcon size={20} className="shrink-0" />,
  },
  warning: {
    alertClass: 'alert-warning text-warning-content',
    icon: <WarningIcon size={20} className="shrink-0" />,
  },
  error: {
    alertClass: 'alert-error text-error-content',
    icon: <XCircleIcon size={20} className="shrink-0" />,
  },
};

export const ToastItem: React.FC<ToastItemProps> = ({ item }) => {
  const { remove } = useToast();
  const { id, message, type = 'info', duration = 3000 } = item;
  const config = TYPE_CONFIG[type];

  // 지정된 시간(duration)이 경과하면 자동으로 토스트 제거
  useEffect(() => {
    const timer = setTimeout(() => {
      remove(id);
    }, duration);

    return () => clearTimeout(timer);
  }, [id, duration, remove]);

  return (
    <div
      onClick={() => remove(id)}
      /* 2. toast-pop-in 대신 styles.toastPopIn 적용 */
      className={`alert ${config.alertClass} ${styles.toastPopIn} shadow-xl cursor-pointer flex items-center gap-3 py-3 px-4 rounded-xl min-w-70 max-w-sm hover:scale-[1.02] active:scale-[0.98] transition-transform select-none border border-black/5`}
      role="alert"
    >
      {config.icon}
      <span className="text-sm font-semibold break-all text-left grow">{message}</span>
    </div>
  );
};
