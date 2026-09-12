import { useCallback, useContext, useMemo, useRef } from 'react';
import { AgGridReact } from 'ag-grid-react';
import { AttendanceContext } from '@/features/attandance/contexts/AttendanceContext.ts';
import { useAttendanceApi } from '@/features/attandance/api/useAttendanceApi.ts';
import { useAsync } from '@/hooks';
import { initGetProfilesParams } from '@/features/attandance/constants/initGetProfilesParams.ts';
import { getColumnDefs } from '@/features/attandance/constants/attendanceGridEntries.ts';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';
import { getSundayLocalDateTime } from '@/features/attandance/utils/getSundayLocalDateTime.ts';
import type { AttendanceRequest } from '@/api/generated/attendanceApi.ts';

export const useAttendanceGrid = () => {
  const { attendancesState } = useContext(AttendanceContext);
  const { getProfiles, saveAttendances } = useAttendanceApi(); // updateAttendanceList 추가 가정

  const [attendances, setAttendances] = attendancesState;

  const gridRef = useRef<AgGridReact<AttendanceRowData>>(null);

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
  return { gridRef, attendances, columnDefs, submit };
};
