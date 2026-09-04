import React from 'react';
import { useAppSelector } from '@/store';
import { dialogRegistry } from './dialogRegistry';
import { DialogWindow } from './DialogWindow';

export const DialogContainer: React.FC = () => {
  const activeDialogs = useAppSelector((state) => state.dialog.activeDialogs);

  if (activeDialogs.length === 0) return null;

  return (
    <>
      {activeDialogs.map((dialog) => {
        const ContentComponent = dialogRegistry[dialog.key];

        return (
          <DialogWindow key={dialog.key} dialog={dialog}>
            {ContentComponent ? (
              <ContentComponent {...(dialog.props ?? {})} />
            ) : (
              <div className="py-4 text-center">
                <p className="text-error font-semibold">등록되지 않은 다이얼로그 키입니다: {dialog.key}</p>
                <p className="text-xs text-base-content/60 mt-2">
                  src/components/common/dialog/dialogRegistry.ts 파일에 해당 키와 컴포넌트를 등록해 주세요.
                </p>
              </div>
            )}
          </DialogWindow>
        );
      })}
    </>
  );
};
