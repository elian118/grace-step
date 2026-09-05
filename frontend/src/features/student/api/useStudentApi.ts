import {
  type StudentProfileRequest,
  type StudentProfileSearchDto,
  useDeleteStudentMutation,
  useGetStudentProfileListMutation,
  useRegisterStudentMutation,
  useUpdateStudentMutation,
} from '@/api/generated/studentApi.ts';
import { useToast } from '@/hooks';

export const useStudentApi = () => {
  const [getStudentTrigger, getStudentStatus] = useGetStudentProfileListMutation();
  const [insertStudentTrigger, insertStudentStatus] = useRegisterStudentMutation();
  const [updateStudentTrigger, updateStudentStatus] = useUpdateStudentMutation();
  const [delStudentTrigger, delStudentStatus] = useDeleteStudentMutation();

  const { toast, errorHandler } = useToast();

  const getStudent = async (params: StudentProfileSearchDto) => {
    try {
      return await getStudentTrigger({ studentProfileSearchDto: params }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const insertStudent = async (params: StudentProfileRequest) => {
    try {
      await insertStudentTrigger({ studentProfileRequest: params }).unwrap();
      toast('학생 정보가 등록되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  const updateStudent = async (id: number, params: StudentProfileRequest) => {
    try {
      await updateStudentTrigger({ id, studentProfileRequest: params }).unwrap();
      toast('학생 정보가 수정되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  const delStudent = async (id: number) => {
    try {
      await delStudentTrigger({ id }).unwrap();
      toast('학생 정보가 삭제되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
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
