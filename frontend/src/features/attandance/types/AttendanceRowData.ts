// row 데이터 타입 정의
export type AttendanceRowData = {
  id: number;
  studentName: string;
  schoolName: string;
  gradeLevel: string;
  isPresent: boolean;
  status: 'ATTENDANCE' | 'LATE' | 'ABSENT' | 'EARLY_LEAVE';
  note?: string;
};
