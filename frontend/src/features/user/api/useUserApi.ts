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

export const useUserApi = () => {
  const [getUserTrigger, getUserStatus] = useLazyGetUserQuery();
  const [getUsersTrigger, getUsersStatus] = useLazyGetUserListQuery();
  const [insertUserTrigger, insertUserStatus] = useSignUpMutation();
  const [updateUserTrigger, updateUserStatus] = useUpdateUserMutation();
  const [deleteUserTrigger, deleteUserStatus] = useDeleteUserMutation();

  const { toast } = useToast();

  const getUser = async (id: number) => {
    try {
      return await getUserTrigger({ id }).unwrap();
    } catch (err) {
      console.error(err);
      // toast(err, "error")
    }
  };

  const getUsers = async (params: UserSearchRequestDto) => {
    try {
      return await getUsersTrigger({ dto: params }).unwrap();
    } catch (err) {
      console.error(err);
      // toast(err, "error")
    }
  };

  const insertUser = async (params: UserSignUpRequestDto) => {
    try {
      return await insertUserTrigger({ userSignUpRequestDto: params }).unwrap();
    } catch (err) {
      console.error(err);
      // toast(err, "error")
    }
  };

  const updateUser = async (id: number, params: UserUpdateRequestDto) => {
    try {
      await updateUserTrigger({ id, userUpdateRequestDto: params }).unwrap();
    } catch (err) {
      console.error(err);
      // toast(err, "error")
    }
  };

  const deleteUser = async (id: number) => {
    try {
      return await deleteUserTrigger({ id }).unwrap();
    } catch (err) {
      console.error(err);
      // toast(err, "error")
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
