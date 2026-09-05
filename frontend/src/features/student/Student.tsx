import React from 'react';
import { useDialog } from '@/hooks';

const Student = () => {
  const { open } = useDialog();

  const openProfileForm = () => {
    open('STUDENT_PROFILE_DIALOG', {
      title: '학생 프로필 입력',
      options: {
        size: 'sm',
        preventCloseOnEsc: true,
        preventCloseOnOutsideClick: true,
      },
    });
  };

  return (
    <div>
      <button className="btn btn-sm btn-primary" onClick={openProfileForm}>
        프로필 등록
      </button>
    </div>
  );
};

export default Student;
