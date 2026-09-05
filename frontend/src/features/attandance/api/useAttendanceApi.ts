import {
  type AttendanceRequest,
  type AttendanceSearchRequestDto,
  useDeleteAttendanceMutation,
  useLazyGetAttendanceFilesQuery,
  useLazyGetAttendanceListQuery,
  useSaveOrUpdateAttendanceMutation,
  useUploadAttendanceFileMutation,
} from '@/api/generated/attendanceApi.ts';

export const useAttendanceApi = () => {
  const [getAttendancesTrigger, getAttendancesStatus] = useLazyGetAttendanceListQuery();
  const [getAttendanceFilesTrigger, getAttendanceFilesStatus] = useLazyGetAttendanceFilesQuery();
  const [uploadAttendanceFileTrigger, uploadAttendanceFileStatus] = useUploadAttendanceFileMutation();
  const [updateAttendanceTrigger, updateAttendanceStatus] = useSaveOrUpdateAttendanceMutation();
  const [delAttendanceTrigger, delAttendanceStatus] = useDeleteAttendanceMutation();

  const getAttendances = async (params: AttendanceSearchRequestDto) => {
    try {
      return await getAttendancesTrigger({ dto: params }).unwrap();
    } catch (err) {
      //
    }
  };

  const getAttendanceFiles = async (startDate: string, endDate: string) => {
    try {
      return await getAttendanceFilesTrigger({ startDate, endDate }).unwrap();
    } catch (err) {
      //
    }
  };

  const uploadAttendanceFile = async (file: Blob) => {
    try {
      await uploadAttendanceFileTrigger({ body: { file } }).unwrap();
    } catch (err) {
      //
    }
  };

  const updateAttendance = async (params: AttendanceRequest) => {
    try {
      await updateAttendanceTrigger({ attendanceRequest: params }).unwrap();
    } catch (err) {
      //
    }
  };

  const delAttendance = async (id: number) => {
    try {
      await delAttendanceTrigger({ attendanceId: id }).unwrap();
    } catch (err) {
      //
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
