import type { UserSignUpRequestDto } from '@/api/generated/userApi.ts';

export const signupKeys: (keyof UserSignUpRequestDto)[] = ['name', 'email', 'password', 'phoneNumber', 'role'];

export const signupNms = ['이름', '이메일', '비밀번호', '연락처', '역할'];

export const signupEntries = signupKeys.map((key, idx) => ({ key, label: signupNms[idx] }));
