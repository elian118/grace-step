import {
  useDeleteUserMutation,
  useLazyGetUserListQuery,
  useLazyGetUserQuery,
  type UserSearchRequestDto,
  type UserSignUpRequestDto,
  type UserUpdateRequestDto,
  useSignUpMutation,
  useUpdateUserMutation,
} from '@/api/generated/userApi.ts';
import { useToast } from '@/hooks';
import type { FetchBaseQueryError } from '@reduxjs/toolkit/query';
import type { CustomApiError } from '@/types/CustomApiError.ts';

export const useUserApi = () => {
  const [getUserTrigger, getUserStatus] = useLazyGetUserQuery();
  const [getUsersTrigger, getUsersStatus] = useLazyGetUserListQuery();
  const [insertUserTrigger, insertUserStatus] = useSignUpMutation();
  const [updateUserTrigger, updateUserStatus] = useUpdateUserMutation();
  const [deleteUserTrigger, deleteUserStatus] = useDeleteUserMutation();

  const { toast, errorHandler } = useToast();

  const getUser = async (id: number) => {
    try {
      return await getUserTrigger({ id }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const getUsers = async (params: UserSearchRequestDto) => {
    try {
      return await getUsersTrigger({ dto: params }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const insertUser = async (params: UserSignUpRequestDto) => {
    try {
      await insertUserTrigger({ userSignUpRequestDto: params }).unwrap();
      toast('회원 정보가 등록되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  const updateUser = async (id: number, params: UserUpdateRequestDto) => {
    try {
      await updateUserTrigger({ id, userUpdateRequestDto: params }).unwrap();
      toast('회원 정보가 수정되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  const deleteUser = async (id: number) => {
    try {
      await deleteUserTrigger({ id }).unwrap();
      toast('회원 정보가 삭제되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  return {
    getUser,
    getUsers,
    insertUser,
    updateUser,
    deleteUser,
    getUserStatus,
    getUsersStatus,
    insertUserStatus,
    updateUserStatus,
    deleteUserStatus,
  };
};
