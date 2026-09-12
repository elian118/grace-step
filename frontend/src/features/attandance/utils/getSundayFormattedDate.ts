import { format, startOfWeek } from 'date-fns';

export const getSundayFormattedDate = () => {
  const today = new Date();

  // 이번 주 일요일 구하기
  const thisSunday = startOfWeek(today, { weekStartsOn: 0 });

  return format(thisSunday, 'yyyy.MM.dd');
};
