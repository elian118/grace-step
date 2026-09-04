import React from 'react';

type SpinnerProps = {
  size: 'loading-xs' | 'loading-sm' | 'loading-md' | 'loading-lg' | 'loading-xl';
};

const Spinner = ({ size }: SpinnerProps) => {
  return <span className={`loading loading-spinner loading-xs ${size}`} />;
};

export default Spinner;
