import {
  type Multiple,
  type Single,
  useDeleteFileMutation,
  useLazyDownloadFileQuery,
  useUploadFileMutation,
  useUploadFilesMutation,
} from '@/api/generated/fileApi.ts';

export const useFileApi = () => {
  const [uploadFileTrigger, uploadFileStatus] = useUploadFileMutation();
  const [uploadFilesTrigger, uploadFilesStatus] = useUploadFilesMutation();
  const [downloadFileTrigger, downloadFileStatus] = useLazyDownloadFileQuery();
  const [delFileTrigger, delFileStatus] = useDeleteFileMutation();

  const uploadFile = async (param: Single) => {
    try {
      return await uploadFileTrigger({ single: param }).unwrap();
    } catch (err) {
      //
    }
  };

  const uploadFiles = async (params: Multiple) => {
    try {
      return await uploadFilesTrigger({ multiple: params }).unwrap();
    } catch (err) {
      //
    }
  };

  const downloadFile = async (fileId: number) => {
    try {
      return await downloadFileTrigger({ fileId }).unwrap();
    } catch (err) {
      //
    }
  };

  const delFile = async (fileId: number) => {
    try {
      await delFileTrigger({ fileId }).unwrap();
    } catch (err) {
      //
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
