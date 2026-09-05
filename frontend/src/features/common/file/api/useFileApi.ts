import {
  type Multiple,
  type Single,
  useDeleteFileMutation,
  useLazyDownloadFileQuery,
  useUploadFileMutation,
  useUploadFilesMutation,
} from '@/api/generated/fileApi.ts';
import { useToast } from '@/hooks';

export const useFileApi = () => {
  const [uploadFileTrigger, uploadFileStatus] = useUploadFileMutation();
  const [uploadFilesTrigger, uploadFilesStatus] = useUploadFilesMutation();
  const [downloadFileTrigger, downloadFileStatus] = useLazyDownloadFileQuery();
  const [delFileTrigger, delFileStatus] = useDeleteFileMutation();

  const { toast, errorHandler } = useToast();

  const uploadFile = async (param: Single) => {
    try {
      const res = await uploadFileTrigger({ single: param }).unwrap();
      toast('파일이 저장되었습니다.', 'success');
      return res;
    } catch (err) {
      errorHandler(err);
    }
  };

  const uploadFiles = async (params: Multiple) => {
    try {
      const res = await uploadFilesTrigger({ multiple: params }).unwrap();
      toast('파일이 저장되었습니다.', 'success');
      return res;
    } catch (err) {
      errorHandler(err);
    }
  };

  const downloadFile = async (fileId: number) => {
    try {
      return await downloadFileTrigger({ fileId }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const delFile = async (fileId: number) => {
    try {
      await delFileTrigger({ fileId }).unwrap();
      toast('파일이 삭제되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
    }
  };

  return {
    uploadFile,
    uploadFiles,
    downloadFile,
    delFile,
    uploadFileStatus,
    uploadFilesStatus,
    downloadFileStatus,
    delFileStatus,
  };
};
