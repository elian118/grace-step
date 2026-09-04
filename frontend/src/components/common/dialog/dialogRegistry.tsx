import React from 'react';

// 샘플 다이얼로그 프롭스 타입 정의
export interface SampleDialogContentProps {
  message: string;
  onConfirm?: () => void;
  onCancel?: () => void;
}

// 샘플 다이얼로그 컴포넌트
export const SampleDialogContent: React.FC<SampleDialogContentProps> = ({ message, onConfirm, onCancel }) => {
  return (
    <div className="space-y-4 py-2">
      <p className="text-base text-base-content">{message}</p>
      <div className="modal-action mt-6 flex justify-end gap-2">
        {onCancel && (
          <button className="btn btn-outline btn-sm" onClick={onCancel}>
            취소
          </button>
        )}
        {onConfirm && (
          <button className="btn btn-primary btn-sm" onClick={onConfirm}>
            확인
          </button>
        )}
      </div>
    </div>
  );
};

// 전역 다이얼로그 레지스트리 선언
// eslint-disable-next-line @typescript-eslint/no-explicit-any, react-refresh/only-export-components
export const dialogRegistry: Record<string, React.ComponentType<any>> = {
  SAMPLE_DIALOG: SampleDialogContent,
  // 앞으로 추가될 다른 기능별 다이얼로그들을 여기에 키-값 쌍으로 매핑합니다.
  // 예시:
  // STUDENT_DETAIL: StudentDetailDialog,
  // EXAM_CREATE: ExamCreateDialog,
};

export type DialogKey = keyof typeof dialogRegistry | string;
