import React, { useState } from 'react';
import type { StudentProfileResponse } from '@/api/generated/studentApi.ts';
import { AttendanceContext, type AttendanceContextType } from '@/features/attandance/contexts/AttendanceContext.ts';
import AttendanceGridContainer from '@/features/attandance/components/AttendanceGridContainer.tsx';

const Attendance = () => {
  const [attendances, setAttendances] = useState<
    StudentProfileResponse &
      { status: 'ATTENDANCE' | 'LATE' | 'ABSENT' | 'EARLY_LEAVE'; isPresent: boolean; note: string }[]
  >([]);

  const value: AttendanceContextType = {
    attendancesState: [attendances, setAttendances],
  };

  return (
    <AttendanceContext.Provider value={value}>
      <AttendanceGridContainer />
    </AttendanceContext.Provider>
  );
};

export default Attendance;
