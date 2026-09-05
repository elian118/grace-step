import {
  type AttendanceRequest,
  type AttendanceSearchRequestDto,
  useDeleteAttendanceMutation,
  useLazyGetAttendanceFilesQuery,
  useLazyGetAttendanceListQuery,
  useSaveOrUpdateAttendanceMutation,
  useUploadAttendanceFileMutation,
} from '@/api/generated/attendanceApi.ts';
import { useToast } from '@/hooks';

export const useAttendanceApi = () => {
  const [getAttendancesTrigger, getAttendancesStatus] = useLazyGetAttendanceListQuery();
  const [getAttendanceFilesTrigger, getAttendanceFilesStatus] = useLazyGetAttendanceFilesQuery();
  const [uploadAttendanceFileTrigger, uploadAttendanceFileStatus] = useUploadAttendanceFileMutation();
  const [updateAttendanceTrigger, updateAttendanceStatus] = useSaveOrUpdateAttendanceMutation();
  const [delAttendanceTrigger, delAttendanceStatus] = useDeleteAttendanceMutation();

  const { toast, errorHandler } = useToast();

  const getAttendances = async (params: AttendanceSearchRequestDto) => {
    try {
      return await getAttendancesTrigger({ dto: params }).unwrap();
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

  const uploadAttendanceFile = async (file: Blob) => {
    try {
      await uploadAttendanceFileTrigger({ body: { file } }).unwrap();
      toast('출석부 파일을 생성했습니다.', 'success');
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
    uploadAttendanceFile,
    updateAttendance,
    delAttendance,
    getAttendancesStatus,
    getAttendanceFilesStatus,
    uploadAttendanceFileStatus,
    updateAttendanceStatus,
    delAttendanceStatus,
  };
};
