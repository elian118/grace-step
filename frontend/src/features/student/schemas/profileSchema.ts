import { z } from 'zod';
import { GRADES } from '@/features/student/constants/GRADES.ts';

export const profileSchema = z.object({
  userId: z.coerce.number({ message: '회원 아이디(숫자)를 입력해주세요.' }),
  teacherId: z.coerce.number({ message: '강사 아이디(숫자)를 입력해주세요.' }),
  schoolName: z.string().min(1, { message: '학교명을 입력해 주세요.' }),
  gradeLevel: z.enum(GRADES, { message: '학년을 선택해 주세요.' }),
  parentPhoneNumber: z.string().optional().nullable(),
  memo: z.string().optional().nullable(),
});

export type ProfileFormData = z.infer<typeof profileSchema>;
