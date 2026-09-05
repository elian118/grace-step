export const gradeOptKeys = [
  'ELEM_1',
  'ELEM_2',
  'ELEM_3',
  'ELEM_4',
  'ELEM_5',
  'ELEM_6',
  'MIDDLE_1',
  'MIDDLE_2',
  'MIDDLE_3',
  'HIGH_1',
  'HIGH_2',
  'HIGH_3',
];
export const gradeOptNms = ['초1', '초2', '초3', '초4', '초5', '초6', '중1', '중2', '중3', '고1', '고2', '고3'];
export const gradeOpts = gradeOptKeys.map((value, idx) => ({ value, label: gradeOptNms[idx] }));
