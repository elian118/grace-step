import {
  type ExamRequestDto,
  useGenerateAndSaveExamMutation,
  useLazyGetExamByTitleQuery,
  useUploadExamPdfMutation,
} from '@/api/generated/examApi.ts';

export const useExamApi = () => {
  const [getExamTrigger, getExamStatus] = useLazyGetExamByTitleQuery();
  const [generateExamTrigger, generateExamStatus] = useGenerateAndSaveExamMutation();
  const [uploadExamPdfTrigger, uploadExamPdfStatus] = useUploadExamPdfMutation();

  const getExam = async (title: string) => {
    try {
      return await getExamTrigger({ title: title }).unwrap();
    } catch (err) {
      //
    }
  };

  const generateExam = async (params: ExamRequestDto) => {
    try {
      await generateExamTrigger({ examRequestDto: params }).unwrap();
    } catch (err) {
      //
    }
  };

  const uploadExamPdf = async (title: string, file: Blob) => {
    try {
      return await uploadExamPdfTrigger({ title, body: { file } }).unwrap();
    } catch (err) {
      //
    }
  };

  return {
    getExam,
    generateExam,
    uploadExamPdf,
    getExamStatus,
    generateExamStatus,
    uploadExamPdfStatus,
  };
};
