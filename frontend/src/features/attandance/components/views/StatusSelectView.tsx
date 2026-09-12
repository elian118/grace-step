import React from 'react';
import { STATUS_OPTIONS } from '@/features/attandance/constants/STATUS_OPTIONS.ts';

const StatusSelectView = (params: any) => {
  const currentStatus = params.data?.status;

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newValue = e.target.value;
    const isPresent = e.target.value === 'ATTENDANCE';
    params.node.setDataValue(params.colDef.field, newValue);
    params.node.setDataValue('isPresent', isPresent);
  };

  return (
    <div>
      <select
        disabled={currentStatus === 'ATTENDANCE'}
        value={params.value}
        className="select select-sm"
        onChange={(e) => handleChange(e)}
      >
        {STATUS_OPTIONS.map((opt, idx) => (
          <option key={`${opt.value}-${opt.label}-${idx}`} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
    </div>
  );
};

export default StatusSelectView;
