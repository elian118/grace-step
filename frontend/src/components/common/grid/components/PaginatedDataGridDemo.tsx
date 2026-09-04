import React, { useState } from 'react';
import { DataGrid } from '../DataGrid';
import { Pagination } from './Pagination.tsx';
import type { ColDef } from 'ag-grid-community';

// 예제 데이터 구조
interface User {
  id: number;
  name: string;
  role: string;
}

const columnDefs: ColDef<User>[] = [
  { field: 'id', headerName: 'ID' },
  { field: 'name', headerName: '이름' },
  { field: 'role', headerName: '역할' },
];

export const PaginatedDataGridDemo = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const totalPage = 5; // BE에서 받은 메타데이터라고 가정

  // 실제 구현 시 여기서 BE에 데이터를 요청하거나
  // 상위 컴포넌트에서 데이터를 받아옵니다.
  const rowData: User[] = [
    { id: 1, name: '사용자 1', role: 'Admin' },
    { id: 2, name: '사용자 2', role: 'User' },
  ];

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    console.log(`요청할 페이지: ${page}`);
    // 여기서 API 호출 등을 수행합니다.
  };

  return (
    <div className="w-full">
      <DataGrid rowData={rowData} columnDefs={columnDefs} />
      <Pagination currentPage={currentPage} totalPage={totalPage} onPageChange={handlePageChange} />
    </div>
  );
};
