import React from 'react';
import { GradeLevel } from '@/features/attandance/constants/GradeLevel.ts';

const GradeLevelView = (params: any) => {
  return <div>{GradeLevel.find((e) => e.value === params.value)?.label}</div>;
};

export default GradeLevelView;
