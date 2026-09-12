import React from 'react';

const AttendanceToggleView = (params: any) => {
  const currentStatus = params.data?.status;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.checked ? 'ATTENDANCE' : 'ABSENT';

    if (params.node) {
      params.node.setDataValue('status', newValue);
      params.node.setDataValue('isPresent', e.target.checked);
    }
  };

  return (
    <div>
      <input className="toggle" type="checkbox" checked={currentStatus === 'ATTENDANCE'} onChange={handleChange} />
    </div>
  );
};

export default AttendanceToggleView;
