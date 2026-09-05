import type { StudentProfileResponse } from '@/api/generated/studentApi.ts';
import type { AttendanceRequest } from '@/api/generated/attendanceApi.ts';
import { type ColDef } from 'ag-grid-community';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';
import { STATUS_OPTIONS } from '@/features/attandance/constants/STATUS_OPTIONS.ts';

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
): ColDef<AttendanceRowData>[] => {
  return attendanceGridEntries.map(({ key, label }) => {
    const fieldKey = key as keyof AttendanceRowData;

    const baseCol: ColDef<AttendanceRowData> = {
      field: fieldKey,
      headerName: label,
      editable: true,
      onCellValueChanged: (params) => {
        const rowIndex = params.node?.rowIndex;
        if (typeof rowIndex === 'number' && onCellDataChange) {
          onCellDataChange(rowIndex, fieldKey, params.newValue);
        }
      },
    };

    switch (key) {
      case 'id':
      case 'studentName':
      case 'schoolName':
      case 'gradeLevel':
        return {
          ...baseCol,
          editable: false,
        };

      case 'isPresent':
        return {
          ...baseCol,
          cellRenderer: 'agCheckboxCellRenderer',
          cellEditor: 'agCheckboxCellEditor',
          editable: true,
          onCellValueChanged: (params) => {
            const rowIndex = params.node?.rowIndex;

            if (rowIndex !== null && rowIndex !== undefined && onCellDataChange) {
              onCellDataChange(rowIndex, 'isPresent', params.newValue);
            }

            params.api.refreshCells({
              rowNodes: [params.node!],
              columns: ['status'],
              force: true,
            });
          },
        };

      case 'status':
        return {
          ...baseCol,
          editable: (params) => !params.data?.isPresent,
          cellEditor: 'agSelectCellEditor',
          cellEditorParams: {
            values: STATUS_OPTIONS.map((opt) => opt.value),
          },
          cellStyle: (params) => {
            if (params.data?.isPresent) {
              return { backgroundColor: '#f3f4f6', color: '#9ca3af', cursor: 'not-allowed' };
            }
            return null;
          },
          valueFormatter: (params) => {
            if (!params.value) return '';
            const found = STATUS_OPTIONS.find((opt) => opt.value === params.value);
            return found ? found.label : String(params.value);
          },
        };

      case 'note':
        return {
          ...baseCol,
          cellEditor: 'agTextCellEditor',
        };

      default:
        return baseCol;
    }
  });
};
