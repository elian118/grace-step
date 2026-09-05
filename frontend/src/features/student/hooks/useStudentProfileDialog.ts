import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { type ProfileFormData, profileSchema } from '@/features/student/schemas/profileSchema.ts';
import { initProfileParams } from '../constants/initProfileParams';
import { useStudentApi } from '@/features/student/api/useStudentApi.ts';
import { useDialog } from '@/hooks';
import type { StudentProfileRequest } from '@/api/generated/studentApi.ts';

export const useStudentProfileDialog = () => {
  const { insertStudent, insertStudentStatus } = useStudentApi();
  const { closeByKey } = useDialog();

  const {
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm<ProfileFormData>({
    resolver: zodResolver(profileSchema as any),
    defaultValues: initProfileParams,
  });

  const onSubmit = async (data: ProfileFormData) => {
    console.log('폼 제출 성공 데이터:', data);
    await insertStudent(data as StudentProfileRequest);
    closeByKey('STUDENT_PROFILE_DIALOG');
  };

  const handleReset = () => reset(initProfileParams);

  return { register, errors, handleSubmit, onSubmit, handleReset, insertStudentStatus };
};
