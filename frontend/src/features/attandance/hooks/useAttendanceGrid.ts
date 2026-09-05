import { useContext, useMemo, useCallback } from 'react';
import { AttendanceContext } from '@/features/attandance/contexts/AttendanceContext.ts';
import { useAttendanceApi } from '@/features/attandance/api/useAttendanceApi.ts';
import { useAsync } from '@/hooks';
import { initGetProfilesParams } from '@/features/attandance/constants/initGetProfilesParams.ts';
import { getColumnDefs } from '@/features/attandance/constants/attendanceGridEntries.ts';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';

export const useAttendanceGrid = () => {
  const { attendancesState } = useContext(AttendanceContext);
  const { getProfiles } = useAttendanceApi();

  const [attendances, setAttendances] = attendancesState;

  useAsync(async () => {
    setAttendances([]);
    const res = await getProfiles(initGetProfilesParams);
    if (res && res.data) {
      setAttendances(res.data.map((r) => ({ ...r, status: 'ATTENDANCE', isPresent: true })));
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

  return { attendances, columnDefs };
};
