import React from 'react';

interface GridLoadingOverlayProps {
  message?: string;
}

export const GridLoadingOverlay: React.FC<GridLoadingOverlayProps> = ({ 
  message = '데이터를 불러오는 중입니다...' 
}) => {
  return (
    <div className="absolute inset-0 z-10 flex flex-col items-center justify-center bg-base-100/60 backdrop-blur-[1px]">
      <div className="flex flex-col items-center gap-3">
        <span className="loading loading-spinner loading-lg text-primary"></span>
        <p className="text-base-content font-medium text-sm">{message}</p>
      </div>
    </div>
  );
};
