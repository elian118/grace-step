import React from 'react';
import { DataGrid } from '@/components/common/grid/DataGrid.tsx';
import { useAttendanceGrid } from '@/features/attandance/hooks/useAttendanceGrid.ts';
import type { AttendanceRowData } from '@/features/attandance/types/AttendanceRowData.ts';
import { getSundayFormattedDate } from '@/features/attandance/utils/getSundayFormattedDate.ts';
import { ClassGrade } from '@/features/attandance/constants/ClassGrade.ts';

const AttendanceGridContainer = () => {
  const { currentTime, searchParams, handleClassGradeSelect, captureRef, gridRef, attendances, columnDefs, submit } =
    useAttendanceGrid();

  return (
    <div className="h-full">
      <div ref={captureRef} className="p-2 bg-gray-100 dark:bg-gray-800">
        <div className="flex flex-rowpx-2 gap-2 justify-between items-center">
          <div className="flex flex-row gap-4 items-end w-1/3">
            <select
              value={searchParams.classGrade}
              className="flex flex-row justify-start items-center w-24 select select-sm"
              onChange={handleClassGradeSelect}
            >
              {ClassGrade.map((c, idx) => (
                <option key={`${c.value}-${c.label}-${idx}`} value={c.value}>
                  {c.label}
                </option>
              ))}
            </select>
          </div>
          <span className="w-1/3 font-bold text-2xl">출석부</span>
          <div className="w-1/3 flex flex-col py-2 justify-end items-end">
            <span className="font-bold text-md">{getSundayFormattedDate()}</span>
            <span className="font-medium text-md">발행시각: {currentTime}</span>
          </div>
        </div>
        <DataGrid<AttendanceRowData>
          ref={gridRef} // <- gridRef 전달
          rowHeight={36}
          rowData={attendances as AttendanceRowData[]}
          columnDefs={columnDefs}
        />
      </div>
      <div className="mt-6 w-full flex justify-end gap-2">
        {/*<button className="btn btn-sm btn-secondary" onClick={handlePreviewCapture}>*/}
        {/*  캡처 미리보기*/}
        {/*</button>*/}
        <button className="btn btn-sm btn-primary" onClick={submit}>
          제출
        </button>
      </div>
    </div>
  );
};

export default AttendanceGridContainer;
