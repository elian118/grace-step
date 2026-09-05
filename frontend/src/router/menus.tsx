import Attendance from '@/features/attandance/Attendance';
import Home from '@/features/home/Home.tsx';
import Classroom from '@/features/classroom/Classroom.tsx';
import Exam from '@/features/exam/Exam';
import Student from '@/features/student/Student.tsx';
import User from '@/features/user/User.tsx';

export const menus = [
  { path: 'home', label: '홈', element: <Home /> },
  { path: 'attendance', label: '출석', element: <Attendance /> },
  { path: 'classroom', label: '교실', element: <Classroom /> },
  { path: 'exam', label: '시험', element: <Exam /> },
  { path: 'student', label: '학생', element: <Student /> },
  { path: 'user', label: '회원', element: <User /> },
];
