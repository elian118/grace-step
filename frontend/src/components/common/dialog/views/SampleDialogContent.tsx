export type SampleDialogContentProps = {
  message: string;
  onConfirm?: () => void;
  onCancel?: () => void;
};

// 샘플 다이얼로그 컴포넌트
export const SampleDialogContent = ({ message, onConfirm, onCancel }: SampleDialogContentProps) => {
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
