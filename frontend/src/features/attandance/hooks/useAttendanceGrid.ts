import { useCallback, useContext, useMemo, useRef, useState } from 'react';
import { AgGridReact } from 'ag-grid-react';
import { AttendanceContext } from '@/features/attandance/contexts/AttendanceContext.ts';
import { useAttendanceApi } from '@/features/attandance/api/useAttendanceApi.ts';
import { useAsync, useDialog } from '@/hooks';
import { initGetProfilesParams } from '@/features/attandance/constants/initGetProfilesParams.ts';
import { getColumnDefs } from '@/features/attandance/constants/attendanceGridEntries.ts';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';
import { getSundayLocalDateTime } from '@/features/attandance/utils/getSundayLocalDateTime.ts';
import type { AttendanceRequest } from '@/api/generated/attendanceApi.ts';
import * as htmlToImage from 'html-to-image';

export const useAttendanceGrid = () => {
  const [previewImageUrl, setPreviewImageUrl] = useState<string | null>(null);
  const { open } = useDialog();
  const { attendancesState } = useContext(AttendanceContext);
  const { getProfiles, saveAttendances } = useAttendanceApi(); // updateAttendanceList 추가 가정

  const [attendances, setAttendances] = attendancesState;

  const captureRef = useRef<HTMLDivElement>(null);
  const gridRef = useRef<AgGridReact<AttendanceRowData>>(null);

  const handlePreviewCapture = async () => {
    if (!captureRef.current) return;

    try {
      const dataUrl = await htmlToImage.toPng(captureRef.current, {
        quality: 1.0,
        pixelRatio: 2, // 고해상도
      });

      // 획득한 Base64 URL을 상태에 저장
      setPreviewImageUrl(dataUrl);
      openPreview(dataUrl);
    } catch (error) {
      console.error('캡처 중 오류 발생:', error);
      alert('이미지 생성에 실패했습니다.');
    }
  };

  const openPreview = (dataUrl: string) => {
    open('CAPTURE_PREVIEW', {
      title: '미리보기',
      props: {
        previewImageUrl: dataUrl,
        setPreviewImageUrl,
      },
      options: {
        size: 'xl',
      },
    });
  };

  const submit = async () => {
    const params: AttendanceRequest[] = attendances.map((e) => ({
      studentProfileId: e.id!,
      attendanceDate: getSundayLocalDateTime(),
      status: e.status,
      note: e.note,
    }));

    // console.log('최종 추출된 payload:', params);
    if (params.length > 0) await saveAttendances(params);
  };

  useAsync(async () => {
    setAttendances([]);
    const res = await getProfiles(initGetProfilesParams);
    if (res && res.data) {
      setAttendances(res.data.map((r) => ({ ...r, status: 'ATTENDANCE', isPresent: true, note: '' })));
    }
  }, []);

  const handleCellDataChange = useCallback(
    (rowIndex: number, field: keyof AttendanceRowData, value: AttendanceRowData[keyof AttendanceRowData]) => {
      setAttendances((prev) => {
        const next = [...prev];
        next[rowIndex] = {
          ...next[rowIndex],
          [field]: value,
        };
        return next;
      });
    },
    [setAttendances],
  );

  const columnDefs = useMemo(() => getColumnDefs(handleCellDataChange), [handleCellDataChange]);

  // gridRef도 반환값에 포함
  return {
    captureRef,
    previewImageUrl,
    setPreviewImageUrl,
    openPreview,
    handlePreviewCapture,
    gridRef,
    attendances,
    columnDefs,
    submit,
  };
};
