import { type SignupFormData, signupSchema } from '@/features/user/schemas/signupSchema.ts';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { initSingUpParams } from '@/features/user/constants/initSingupParams.ts';
import { useUserApi } from '@/features/user/api/useUserApi.ts';
import type { UserSignUpRequestDto } from '@/api/generated/userApi.ts';
import { useDialog } from '@/hooks';

export const useSignUpDialog = () => {
  const { insertUser, insertUserStatus } = useUserApi();
  const { closeByKey } = useDialog();

  const {
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm<SignupFormData>({
    resolver: zodResolver(signupSchema),
    defaultValues: initSingUpParams,
  });

  const onSubmit = async (data: SignupFormData) => {
    console.log('폼 제출 성공 데이터:', data);
    await insertUser(data as UserSignUpRequestDto);
    closeByKey('SIGN_UP_DIALOG');
  };

  const handleReset = () => reset(initSingUpParams);

  return { register, errors, handleSubmit, onSubmit, handleReset, insertUserStatus };
};
