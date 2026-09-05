import React from 'react';
import { signupEntries } from '@/features/user/constants/signupEntries.ts';
import { roleOpts } from '@/features/user/constants/roleOptions.ts';
import { useSignUpDialog } from '@/features/user/hooks/useSignUpDialog.ts';

const SignUpDialogContent = () => {
  const { register, errors, handleSubmit, onSubmit, handleReset } = useSignUpDialog();

  return (
    <form onSubmit={handleSubmit(onSubmit)} autoComplete="off">
      <div className="mb-10">
        {signupEntries.map((e) => (
          <fieldset key={`signup-form-${e.key}`} className="fieldset">
            <legend className="fieldset-legend">{e.label}</legend>
            {e.key === 'role' ? (
              <select className="select" {...register(e.key)}>
                {roleOpts.map((role) => (
                  <option key={`role-${role.value}`} value={role.value}>
                    {role.label}
                  </option>
                ))}
              </select>
            ) : (
              <input
                id={e.key}
                className="input"
                type={e.key === 'email' || e.key === 'password' ? e.key : 'text'}
                {...register(e.key)}
                placeholder={
                  e.key === 'password'
                    ? '최소 8자 이상 입력하세요.'
                    : e.key === 'phoneNumber'
                      ? '숫자만 입력하세요'
                      : undefined
                }
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

export default SignUpDialogContent;
