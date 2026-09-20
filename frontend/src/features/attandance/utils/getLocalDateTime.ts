import { format } from 'date-fns';

export const getLocalTime = () => {
  const today = new Date();

  // 00:00:00 자정 기준 ISO Local Date-Time 포맷
  return format(today, 'HH:mm:ss');
};
