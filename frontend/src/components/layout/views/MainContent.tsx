import React from 'react';
import { Outlet } from 'react-router-dom';

const MainContent = () => {
  return (
    <main className="flex-1 p-6 md:p-10 2xl:max-h-200 h-full bg-gray-100 dark:bg-gray-800">
      <div className="max-w-4xl mx-auto py-2 text-center">
        <Outlet />
      </div>
    </main>
  );
};

export default MainContent;
