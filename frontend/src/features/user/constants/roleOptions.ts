export const roleOptKeys = ['STUDENT', 'TEACHER', 'STAFF', 'ADMIN'];
export const roleOptNms = ['학생', '강사', '관계자', '관리자'];
export const roleOpts = roleOptKeys.map((value, idx) => ({ label: roleOptNms[idx], value }));
