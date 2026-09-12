import React from 'react';
import SignUpDialogContent from '@/features/user/components/dialogs/SignUpDialogContent.tsx';
import { SampleDialogContent } from '@/components/common/dialog/views/SampleDialogContent.tsx';
import StudentProfileDialogContent from '@/features/student/components/dialogs/StudentProfileDialogContent.tsx';
import CapturePreviewContent from '@/features/attandance/components/dialogs/CapturePreviewContent.tsx';

// 전역 다이얼로그 레지스트리 선언
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const dialogRegistry: Record<string, React.ComponentType<any>> = {
  SAMPLE_DIALOG: SampleDialogContent,
  SIGN_UP_DIALOG: SignUpDialogContent,
  STUDENT_PROFILE_DIALOG: StudentProfileDialogContent,
  CAPTURE_PREVIEW: CapturePreviewContent,
};

export type DialogKey = keyof typeof dialogRegistry | string;
