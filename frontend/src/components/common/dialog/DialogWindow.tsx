import React, { useEffect, useRef, useState } from 'react';
import type { DialogInstance } from '@/hooks/useDialog';
import { useDialog } from '@/hooks/useDialog';
import { XIcon } from '@phosphor-icons/react';

interface DialogWindowProps {
  dialog: DialogInstance;
  children: React.ReactNode;
}

const SIZE_CLASSES = {
  xs: 'max-w-xs',
  sm: 'max-w-sm',
  md: 'max-w-md',
  lg: 'max-w-lg',
  xl: 'max-w-xl',
  '2xl': 'max-w-2xl',
  '3xl': 'max-w-3xl',
  '4xl': 'max-w-4xl',
  '5xl': 'max-w-5xl',
  '6xl': 'max-w-6xl',
  '7xl': 'max-w-7xl',
  full: 'max-w-[95vw] w-full',
};

export const DialogWindow: React.FC<DialogWindowProps> = ({ dialog, children }) => {
  const { closeByKey } = useDialog();
  const { key, title, options } = dialog;

  const sizeClass = options?.size ? SIZE_CLASSES[options.size] : SIZE_CLASSES.md;
  const preventCloseOnEsc = options?.preventCloseOnEsc ?? false;
  const preventCloseOnOutsideClick = options?.preventCloseOnOutsideClick ?? false;
  const blurBackground = options?.blurBackground ?? false;
  const allowDrag = options?.allowDrag ?? false;

  // 드래그 위치 제어를 위한 상태
  const [position, setPosition] = useState({ x: 0, y: 0 });
  const [isDragging, setIsDragging] = useState(false);
  const dragStart = useRef({ x: 0, y: 0 });
  const windowRef = useRef<HTMLDivElement>(null);

  // 헤더 영역 드래그 다운 핸들러
  const handleMouseDown = (e: React.MouseEvent<HTMLDivElement>) => {
    if (!allowDrag) return;
    const target = e.target as HTMLElement;

    // 대화상자 내 입력 요소, 선택 요소 및 버튼을 조작할 때는 드래그를 허용하지 않음
    if (target.closest('button') || target.closest('input') || target.closest('textarea') || target.closest('select')) {
      return;
    }

    setIsDragging(true);
    dragStart.current = {
      x: e.clientX - position.x,
      y: e.clientY - position.y,
    };
  };

  // 마우스 이동 및 버튼 뗌 감지를 위한 글로벌 리스너
  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      if (!isDragging) return;
      setPosition({
        x: e.clientX - dragStart.current.x,
        y: e.clientY - dragStart.current.y,
      });
    };

    const handleMouseUp = () => {
      setIsDragging(false);
    };

    if (isDragging) {
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);
    }

    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseup', handleMouseUp);
    };
  }, [isDragging]);

  // ESC 키 이벤트 감지 및 처리
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        if (!preventCloseOnEsc) {
          closeByKey(key);
        }
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [key, preventCloseOnEsc, closeByKey]);

  // 바깥(배경) 클릭 이벤트 처리
  const handleBackdropClick = (e: React.MouseEvent<HTMLDialogElement>) => {
    if (e.target === e.currentTarget && !preventCloseOnOutsideClick) {
      closeByKey(key);
    }
  };

  return (
    <dialog
      className={`modal modal-open transition-all duration-200 ${
        blurBackground ? 'backdrop-blur-sm bg-black/30' : 'bg-black/20'
      }`}
      onClick={handleBackdropClick}
      style={{ zIndex: 1000 }}
    >
      <div
        ref={windowRef}
        className={`modal-box relative ${sizeClass} overflow-visible transition-none`}
        style={{
          transform: `translate(${position.x}px, ${position.y}px)`,
          cursor: isDragging ? 'grabbing' : 'default',
        }}
      >
        {/* 헤더 영역 */}
        <div
          className={`flex items-center justify-between pb-3 border-b border-base-200 select-none ${
            allowDrag ? 'cursor-move' : 'cursor-default'
          }`}
          onMouseDown={handleMouseDown}
        >
          <h3 className="font-bold text-lg text-base-content">{title || '알림'}</h3>
          <button className="btn btn-sm btn-circle btn-ghost" onClick={() => closeByKey(key)} aria-label="닫기">
            <XIcon size={18} />
          </button>
        </div>

        {/* 다이얼로그 핵심 콘텐츠 */}
        <div className="mt-4">{children}</div>
      </div>
    </dialog>
  );
};
