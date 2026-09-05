import {
  type AttendanceRequest,
  type AttendanceSearchRequestDto,
  useDeleteAttendanceMutation,
  useLazyGetAttendanceListQuery,
  useSaveOrUpdateAttendanceMutation,
} from '@/api/generated/studentApi.ts';

export const useAttendanceApi = () => {
  const [getAttendancesTrigger, getAttendancesStatus] = useLazyGetAttendanceListQuery();
  const [updateAttendanceTrigger, updateAttendanceStatus] = useSaveOrUpdateAttendanceMutation();
  const [delAttendanceTrigger, delAttendanceStatus] = useDeleteAttendanceMutation();

  const getAttendances = async (params: AttendanceSearchRequestDto) => {
    try {
      return await getAttendancesTrigger({ dto: params }).unwrap();
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
    updateAttendance,
    delAttendance,
    getAttendancesStatus,
    updateAttendanceStatus,
    delAttendanceStatus,
  };
};
