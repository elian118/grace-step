import {
  type StudentProfileRequest,
  type StudentProfileSearchDto,
  useDeleteStudentMutation,
  useLazyGetStudentProfileListQuery,
  useRegisterStudentMutation,
  useUpdateStudentMutation,
} from '@/api/generated/studentApi.ts';

export const useStudentApi = () => {
  const [getStudentTrigger, getStudentStatus] = useLazyGetStudentProfileListQuery();
  const [insertStudentTrigger, insertStudentStatus] = useRegisterStudentMutation();
  const [updateStudentTrigger, updateStudentStatus] = useUpdateStudentMutation();
  const [delStudentTrigger, delStudentStatus] = useDeleteStudentMutation();

  const getStudent = async (params: StudentProfileSearchDto) => {
    try {
      return await getStudentTrigger({ dto: params }).unwrap();
    } catch (err) {
      //
    }
  };

  const insertStudent = async (params: StudentProfileRequest) => {
    try {
      await insertStudentTrigger({ studentProfileRequest: params }).unwrap();
    } catch (err) {
      //
    }
  };

  const updateStudent = async (id: number, params: StudentProfileRequest) => {
    try {
      await updateStudentTrigger({ id, studentProfileRequest: params }).unwrap();
    } catch (err) {
      //
    }
  };

  const delStudent = async (id: number) => {
    try {
      await delStudentTrigger({ id }).unwrap();
    } catch (err) {
      //
    }
  };

  return {
    getStudent,
    insertStudent,
    updateStudent,
    delStudent,
    getStudentStatus,
    insertStudentStatus,
    updateStudentStatus,
    delStudentStatus,
  };
};
