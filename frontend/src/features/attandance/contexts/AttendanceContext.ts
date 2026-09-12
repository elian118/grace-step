import { createContext, type Dispatch, type SetStateAction } from 'react';
import type { StudentProfileResponse } from '@/api/generated/studentApi.ts';

export type AttendanceContextType = {
  attendancesState: [
    (StudentProfileResponse & {
      status: 'ATTENDANCE' | 'LATE' | 'ABSENT' | 'EARLY_LEAVE';
      isPresent: boolean;
      note: string;
    })[],
    Dispatch<
      SetStateAction<
        StudentProfileResponse &
          {
            status: 'ATTENDANCE' | 'LATE' | 'ABSENT' | 'EARLY_LEAVE';
            isPresent: boolean;
            note: string;
          }[]
      >
    >,
  ];
};

const initAttendanceContext: AttendanceContextType = {
  attendancesState: [[], () => {}],
};

export const AttendanceContext = createContext<AttendanceContextType>(initAttendanceContext);
