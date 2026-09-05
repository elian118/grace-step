import type { StudentProfileRequest } from '@/api/generated/studentApi.ts';

export const profileKeys: (keyof StudentProfileRequest)[] = [
  'userId',
  'schoolName',
  'gradeLevel',
  'parentPhoneNumber',
  'teacherId',
  'memo',
];

export const profileNms = ['아이디', '학교명', '학년', '학부모 연락처', '담당강사 아이디', '메모'];

export const profileEntries = profileKeys.map((key, idx) => ({ key, label: profileNms[idx] }));
