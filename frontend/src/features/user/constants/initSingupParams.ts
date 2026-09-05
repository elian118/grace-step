import type { UserSignUpRequestDto } from '@/api/generated/userApi.ts';

export const initSingUpParams: UserSignUpRequestDto = {
  name: '',
  email: '',
  password: '',
  phoneNumber: '',
  role: 'STUDENT',
};
