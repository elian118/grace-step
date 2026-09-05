import type { StudentProfileSearchDto } from '@/api/generated/studentApi.ts';

export const initGetProfilesParams: StudentProfileSearchDto = {
  name: '',
  isActive: true,
  page: 1,
  size: 10,
};
