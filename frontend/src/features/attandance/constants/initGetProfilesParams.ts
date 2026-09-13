import type { StudentProfileSearchDto } from '@/api/generated/studentApi.ts';

export const initGetProfilesParams: StudentProfileSearchDto = {
  name: '',
  isActive: true,
  classGrade: 'ELEM',
  page: 1,
  size: 10,
};
