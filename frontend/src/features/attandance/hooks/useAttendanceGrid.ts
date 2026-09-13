import React, { useCallback, useContext, useMemo, useRef, useState } from 'react';
import { AgGridReact } from 'ag-grid-react';
import { AttendanceContext } from '@/features/attandance/contexts/AttendanceContext.ts';
import { useAttendanceApi } from '@/features/attandance/api/useAttendanceApi.ts';
import { useAsync } from '@/hooks';
import { initGetProfilesParams } from '@/features/attandance/constants/initGetProfilesParams.ts';
import { getColumnDefs } from '@/features/attandance/constants/attendanceGridEntries.ts';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';
import { getSundayLocalDateTime } from '@/features/attandance/utils/getSundayLocalDateTime.ts';
import type { AttendanceRequest } from '@/api/generated/attendanceApi.ts';
import * as htmlToImage from 'html-to-image';
import type { PageResponseStudentProfileResponse, StudentProfileSearchDto } from '@/api/generated/studentApi.ts';
import { dataURLtoFile } from '@/utils/dataURLtoFile.ts';
import { ClassGrade } from '@/features/attandance/constants/ClassGrade.ts';

export const useAttendanceGrid = () => {
  const [searchParams, setSearchParams] = useState<StudentProfileSearchDto>(initGetProfilesParams);
  const { attendancesState } = useContext(AttendanceContext);
  const { getProfiles, saveAttendances, uploadAttendanceFile } = useAttendanceApi(); // updateAttendanceList 추가 가정

  const [attendances, setAttendances] = attendancesState;

  const captureRef = useRef<HTMLDivElement>(null);
  const gridRef = useRef<AgGridReact<AttendanceRowData>>(null);

  const handlePreviewCapture = async () => {
    if (!captureRef.current) return;

    try {
      return await htmlToImage.toPng(captureRef.current, {
        quality: 1.0,
        pixelRatio: 2, // 고해상도
      });
    } catch (error) {
      console.error('캡처 중 오류 발생:', error);
      alert('이미지 생성에 실패했습니다.');
    }
  };

  const submit = async () => {
    const params: AttendanceRequest[] = attendances.map((e) => ({
      studentProfileId: e.id!,
      attendanceDate: getSundayLocalDateTime(),
      status: e.status,
      note: e.note,
    }));

    if (params.length > 0) {
      await uploadCapture(); // 출석부 캡처 파일 업로드
      await saveAttendances(params); // 출석기록 DB 저장
    }
  };

  const setInitRows = (res?: PageResponseStudentProfileResponse) => {
    if (res && res.data) {
      setAttendances(res.data.map((r) => ({ ...r, status: 'ATTENDANCE', isPresent: true, note: '' })));
    }
  };

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

  const handleClassGradeSelect = async (e: React.ChangeEvent<HTMLSelectElement>) => {
    console.log(e.target.value);
    const newSearchParams = { ...searchParams, classGrade: e.target.value as 'ELEM' | 'MIDDLE' | 'HIGH' };
    setSearchParams(newSearchParams);
    console.log(newSearchParams);
    const res = await getProfiles(newSearchParams);
    setInitRows(res);
  };

  const columnDefs = useMemo(() => getColumnDefs(handleCellDataChange), [handleCellDataChange]);

  const uploadCapture = async () => {
    const previewImageUrl = await handlePreviewCapture();
    if (!previewImageUrl) return;

    const file = dataURLtoFile(previewImageUrl, `attendance_${Date.now()}.png`);
    await uploadAttendanceFile(
      file,
      `${ClassGrade.find((e) => e.value === searchParams?.classGrade)?.label ?? ''} 출석부`,
    );
  };

  useAsync(async () => {
    setAttendances([]);
    const res = await getProfiles(initGetProfilesParams);
    setInitRows(res);
  }, []);

  return {
    searchParams,
    handleClassGradeSelect,
    captureRef,
    handlePreviewCapture,
    gridRef,
    attendances,
    columnDefs,
    submit,
  };
};
