import React from 'react';
import { DataGrid } from '@/components/common/grid/DataGrid.tsx';
import { useAttendanceGrid } from '@/features/attandance/hooks/useAttendanceGrid.ts';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';
import { getSundayFormattedDate } from '@/features/attandance/utils/getSundayFormattedDate.ts';

const AttendanceGridContainer = () => {
  const { gridRef, attendances, columnDefs, submit } = useAttendanceGrid();

  return (
    <div>
      <div className="py-2 mb-6">
        <span className="font-bold text-2xl">{getSundayFormattedDate()} 출석부</span>
      </div>
      <DataGrid<AttendanceRowData>
        ref={gridRef} // <- gridRef 전달
        rowData={attendances as AttendanceRowData[]}
        columnDefs={columnDefs}
      />
      <div className="mt-6 w-full flex justify-end">
        <button className="btn btn-sm btn-primary" onClick={submit}>
          제출
        </button>
      </div>
    </div>
  );
};

export default AttendanceGridContainer;
