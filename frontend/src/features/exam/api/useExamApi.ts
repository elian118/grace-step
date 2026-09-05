import {
  type ExamRequestDto,
  useGenerateAndSaveExamMutation,
  useLazyGetExamByTitleQuery,
  useUploadExamPdfMutation,
} from '@/api/generated/examApi.ts';
import { useToast } from '@/hooks';

export const useExamApi = () => {
  const [getExamTrigger, getExamStatus] = useLazyGetExamByTitleQuery();
  const [generateExamTrigger, generateExamStatus] = useGenerateAndSaveExamMutation();
  const [uploadExamPdfTrigger, uploadExamPdfStatus] = useUploadExamPdfMutation();

  const { toast, errorHandler } = useToast();

  const getExam = async (title: string) => {
    try {
      return await getExamTrigger({ title: title }).unwrap();
    } catch (err) {
      errorHandler(err);
    }
  };

  const generateExam = async (params: ExamRequestDto) => {
    try {
      await generateExamTrigger({ examRequestDto: params }).unwrap();
    } catch (err) {
      toast('시험지 생성이 완료되었습니다.', 'success');
      errorHandler(err);
    }
  };

  const uploadExamPdf = async (title: string, file: Blob) => {
    try {
      await uploadExamPdfTrigger({ title, body: { file } }).unwrap();
      toast('시험지 파일이 생성되었습니다.', 'success');
    } catch (err) {
      errorHandler(err);
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
