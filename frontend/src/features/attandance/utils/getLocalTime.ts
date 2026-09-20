import { format } from 'date-fns';

import { startOfWeek } from 'date-fns';

export const getSundayLocalDateTime = () => {
  const today = new Date();
  const thisSunday = startOfWeek(today, { weekStartsOn: 0 });

  // 00:00:00 자정 기준 ISO Local Date-Time 포맷
  return format(thisSunday, "yyyy-MM-dd'T'HH:mm:ss");
};
