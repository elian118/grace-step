import React from 'react';
import { Outlet } from 'react-router-dom';

const MainContent = () => {
  return (
    <main className="flex-1 p-6 md:p-10">
      <div className="max-w-3xl mx-auto py-12 text-center">
        <Outlet />
      </div>
    </main>
  );
};

export default MainContent;
