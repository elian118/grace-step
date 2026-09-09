import React from 'react';
import { profileEntries } from '@/features/student/constants/profileEntries.ts';
import { useStudentProfileDialog } from '@/features/student/hooks/useStudentProfileDialog.ts';
import { gradeOpts } from '@/features/student/constants/gradeOpts.ts';

const StudentProfileDialogContent = () => {
  const { register, errors, handleSubmit, onSubmit, handleReset } = useStudentProfileDialog();

  return (
    <form onSubmit={handleSubmit(onSubmit)} autoComplete="off">
      <div className="mb-10">
        {profileEntries.map((e) => (
          <fieldset key={`signup-form-${e.key}`} className="fieldset">
            <legend className="fieldset-legend">{e.label}</legend>
            {e.key === 'gradeLevel' ? (
              <select className="select" {...register(e.key)}>
                {gradeOpts.map((role) => (
                  <option key={`role-${role.value}`} value={role.value}>
                    {role.label}
                  </option>
                ))}
              </select>
            ) : (
              <input
                id={e.key}
                className="input"
                type={e.key === 'userId' || e.key === 'teacherId' ? 'number' : 'text'}
                {...register(e.key)}
                placeholder={e.key === 'parentPhoneNumber' ? '숫자만 입력하세요' : undefined}
              />
            )}

            {errors[e.key] && <p className="text-error text-sm">{errors[e.key]?.message}</p>}
          </fieldset>
        ))}
      </div>
      <div className="divider" />
      <div className="flex gap-2 justify-end items-center">
        <button className="btn btn-primary btn-outline" type="button" onClick={handleReset}>
          초기화
        </button>

        <button className="btn btn-primary" type="submit">
          가입하기
        </button>
      </div>
    </form>
  );
};

export default StudentProfileDialogContent;
