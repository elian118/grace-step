import {
  type AttendanceRequest,
  type AttendanceSearchRequestDto,
  useDeleteAttendanceMutation,
  useGetAttendanceListMutation,
  useLazyGetAttendanceFilesQuery,
  useSaveOrUpdateAttendanceMutation,
  useSaveOrUpdateAttendancesMutation,
  useUploadAttendanceFileMutation,
} from '@/api/generated/attendanceApi.ts';
import { useToast } from '@/hooks';
import { type StudentProfileSearchDto, useGetStudentProfileListMutation } from '@/api/generated/studentApi.ts';

export const useAttendanceApi = () => {
  const [getProfilesTrigger, getProfilesStatus] = useGetStudentProfileListMutation();
  const [getAttendancesTrigger, getAttendancesStatus] = useGetAttendanceListMutation();
  const [getAttendanceFilesTrigger, getAttendanceFilesStatus] = useLazyGetAttendanceFilesQuery();
  const [uploadAttendanceFileTrigger, uploadAttendanceFileStatus] = useUploadAttendanceFileMutation();
  const [updateAttendanceTrigger, updateAttendanceStatus] = useSaveOrUpdateAttendanceMutation();
  const [delAttendanceTrigger, delAttendanceStatus] = useDeleteAttendanceMutation();
  const [saveAttendancesTrigger, saveAttendancesStatus] = useSaveOrUpdateAttendancesMutation();

  const { toast, errorHandler } = useToast();

  const getAttendances = async (params: AttendanceSearchRequestDto) => {
    try {
      return await getAttendancesTrigger({ attendanceSearchRequestDto: params }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const getAttendanceFiles = async (startDate: string, endDate: string) => {
    try {
      return await getAttendanceFilesTrigger({ startDate, endDate }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const getProfiles = async (params: StudentProfileSearchDto) => {
    try {
      return await getProfilesTrigger({ studentProfileSearchDto: params }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const uploadAttendanceFile = async (file: Blob) => {
    try {
      const formData = new FormData();
      formData.append('file', file);

      await uploadAttendanceFileTrigger({ body: formData as any }).unwrap();
      toast('출석부 파일을 생성했습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  const saveAttendances = async (params: AttendanceRequest[]) => {
    try {
      await saveAttendancesTrigger({ body: params }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const updateAttendance = async (params: AttendanceRequest) => {
    try {
      await updateAttendanceTrigger({ attendanceRequest: params }).unwrap();
      toast('출석 기록을 수정했습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  const delAttendance = async (id: number) => {
    try {
      await delAttendanceTrigger({ attendanceId: id }).unwrap();
      toast('출석 기록을 삭제했습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  return {
    getAttendances,
    getAttendanceFiles,
    getProfiles,
    uploadAttendanceFile,
    updateAttendance,
    saveAttendances,
    delAttendance,
    getAttendancesStatus,
    getAttendanceFilesStatus,
    getProfilesStatus,
    uploadAttendanceFileStatus,
    updateAttendanceStatus,
    saveAttendancesStatus,
    delAttendanceStatus,
  };
};
