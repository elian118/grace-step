import React from 'react';
import { useDialog } from '@/hooks';

const User = () => {
  const { open, closeByKey } = useDialog();

  const openSignupDialog = () => {
    open('SIGN_UP_DIALOG', {
      title: '회원등록',
      options: {
        size: 'sm',
        preventCloseOnOutsideClick: true,
        preventCloseOnEsc: true,
      },
    });
  };

  return (
    <div>
      <button className="btn btn-sm btn-neutral" onClick={openSignupDialog}>
        회원등록
      </button>
    </div>
  );
};

export default User;
