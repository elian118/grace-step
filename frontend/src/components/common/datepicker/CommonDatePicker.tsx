import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

export const CommonDatePicker = () => {
  const [startDate, setStartDate] = useState<Date | null>(new Date());

  return (
    <DatePicker
      selected={startDate}
      onChange={(date: Date | null) => setStartDate(date)}
      className="input input-bordered w-full max-w-xs"
      dateFormat="yyyy-MM-dd"
    />
  );
};
