import React from 'react';
import { useDialog } from '@/hooks';
import { useAttendanceApi } from '@/features/attandance/api/useAttendanceApi.ts';
import { dataURLtoFile } from '@/utils/dataURLtoFile.ts';

type CapturePreviewContentProps = {
  previewImageUrl: string;
  setPreviewImageUrl: (val: string | null) => void;
};

const CapturePreviewContent = ({ previewImageUrl, setPreviewImageUrl }: CapturePreviewContentProps) => {
  const { closeByKey } = useDialog();
  const { uploadAttendanceFile } = useAttendanceApi();

  const uploadCapture = async () => {
    if (!previewImageUrl) return;

    const file = dataURLtoFile(previewImageUrl, `attendance_${Date.now()}.png`);
    await uploadAttendanceFile(file);

    setPreviewImageUrl(null);
    closeByKey('CAPTURE_PREVIEW');
  };

  return (
    <div>
      {previewImageUrl && (
        <div className="flex flex-col items-center justify-center">
          <img
            src={previewImageUrl}
            alt="캡처 미리보기"
            style={{
              border: '1px solid #ccc',
              maxWidth: '100%',
              boxShadow: '0 2px 4px rgba(0,0,0,0.2)',
            }}
          />
          <p style={{ color: '#e74c3c', fontSize: '14px', marginTop: '10px' }}>
            &uarr; 위 이미지가 서버로 전송될 최종 결과물입니다. &uarr;
          </p>
          <div className="mt-6 w-full flex flex-row gap-2 items-center justify-end">
            <button className="btn btn-sm btn-primary" onClick={uploadCapture}>
              파일로 저장
            </button>
            <button
              className="w-24 btn btn-sm btn-block"
              onClick={() => {
                setPreviewImageUrl(null);
                closeByKey('CAPTURE_PREVIEW');
              }}
            >
              미리보기 닫기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CapturePreviewContent;
