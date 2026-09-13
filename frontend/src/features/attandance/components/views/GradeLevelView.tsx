import React from 'react';
import { GradeLevel } from '@/features/attandance/constants/GradeLevel.ts';
import { ClassGrade } from '@/features/attandance/constants/ClassGrade.ts';

const GradeLevelView = (params: any) => {
  const field = params.colDef.field;
  const targetOpts = field === 'classGrade' ? ClassGrade : GradeLevel;
  return <div>{targetOpts.find((e) => e.value === params.value)?.label}</div>;
};

export default GradeLevelView;
