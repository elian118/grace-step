import type { StudentProfileResponse } from '@/api/generated/studentApi.ts';
import type { AttendanceRequest } from '@/api/generated/attendanceApi.ts';
import { type ColDef } from 'ag-grid-community';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';
import AttendanceToggleView from '@/features/attandance/components/views/AttendanceToggleView.tsx';
import StatusSelectView from '@/features/attandance/components/views/StatusSelectView.tsx';
import NoteInputView from '@/features/attandance/components/views/NoteInputView.tsx';
import GradeLevelView from '@/features/attandance/components/views/GradeLevelView.tsx';

export const attendanceGridKeys: (keyof StudentProfileResponse | keyof AttendanceRequest | 'isPresent')[] = [
  'id',
  'studentName',
  'schoolName',
  'gradeLevel',
  'isPresent',
  'status',
  'note',
];

export const attendanceGridNms = ['ID', '이름', '학교', '학년', '출석 여부', '출석 상태', '특이사항 및 메모'];

export const attendanceGridEntries = attendanceGridKeys.map((key, idx) => ({
  key,
  label: attendanceGridNms[idx],
}));

export const getColumnDefs = (
  onCellDataChange?: (
    rowIndex: number,
    field: keyof AttendanceRowData,
    value: AttendanceRowData[keyof AttendanceRowData],
  ) => void,
): ColDef<AttendanceRowData>[] =>
  attendanceGridEntries.map(({ key, label }) => {
    const fieldKey = key as keyof AttendanceRowData;

    return {
      field: fieldKey,
      headerName: label,
      editable: true,
      minWidth: key === 'id' ? 60 : key === 'note' ? 200 : 100,
      flex: 1,
      cellRenderer: (params: any) =>
        key === 'gradeLevel'
          ? GradeLevelView(params)
          : key === 'isPresent'
            ? AttendanceToggleView(params)
            : key === 'status'
              ? StatusSelectView(params)
              : key === 'note'
                ? NoteInputView(params)
                : params.value,
    };
  });
