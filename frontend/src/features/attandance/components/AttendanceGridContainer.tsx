import React from 'react';
import { DataGrid } from '@/components/common/grid/DataGrid.tsx';
import { useAttendanceGrid } from '@/features/attandance/hooks/useAttendanceGrid.ts';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';

const AttendanceGridContainer = () => {
  const { attendances, columnDefs } = useAttendanceGrid();

  return (
    <div>
      <DataGrid<AttendanceRowData> rowData={attendances as AttendanceRowData[]} columnDefs={columnDefs} />
    </div>
  );
};

export default AttendanceGridContainer;
