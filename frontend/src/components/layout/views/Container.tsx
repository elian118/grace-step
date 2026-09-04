import type { ReactNode } from 'react';

type ContainerProps = {
  children: ReactNode;
};

export const Container = ({ children }: ContainerProps) => {
  return (
    <div className="min-h-screen bg-base-300 w-full flex justify-center items-center 2xl:p-6">
      <div className="w-full min-h-screen 2xl:min-h-[calc(100vh-8rem)] 2xl:max-w-7xl bg-base-100 2xl:rounded-3xl 2xl:shadow-2xl 2xl:border 2xl:border-base-content/10 flex flex-col overflow-hidden transition-all duration-200">
        {children}
      </div>
    </div>
  );
};
