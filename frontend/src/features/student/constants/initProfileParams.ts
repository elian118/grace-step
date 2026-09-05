import type { StudentProfileRequest } from '@/api/generated/studentApi.ts';

export const initProfileParams: StudentProfileRequest = {
  userId: 3,
  teacherId: 2,
  schoolName: '',
  gradeLevel: 'ELEM_1',
  parentPhoneNumber: '',
  memo: '',
};
