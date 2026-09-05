import { z } from 'zod';
import { ROLES } from '@/features/user/constants/ROLES.ts';

export const signupSchema = z.object({
  name: z.string().min(1, { message: '이름을 입력해 주세요.' }),
  email: z.string().min(1, { message: '이메일을 입력해 주세요.' }).email({ message: '올바른 이메일 형식이 아닙니다.' }),
  password: z.string().min(8, { message: '비밀번호는 최소 8자 이상이어야 합니다.' }),
  phoneNumber: z.string().optional().nullable(),
  role: z.enum(ROLES, { message: '역할을 선택해 주세요.' }),
});

export type SignupFormData = z.infer<typeof signupSchema>;
