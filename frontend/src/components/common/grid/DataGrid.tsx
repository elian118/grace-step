import React, { useMemo } from 'react';
import { AgGridReact } from 'ag-grid-react';
import type { ColDef, GridOptions } from 'ag-grid-community';
import styles from '@/styles/grid/DataGrid.module.css';
import { GridLoadingOverlay } from './components/loading/GridLoadingOverlay';

// AG Grid 필수 모듈 import (프로젝트 설정에 따라 다를 수 있으나 일반적인 방식)
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';

interface DataGridProps<TData> {
  rowData: TData[];
  columnDefs: ColDef<TData>[];
  gridOptions?: GridOptions<TData>;
  isLoading?: boolean;
}

export const DataGrid = <TData,>({ rowData, columnDefs, gridOptions, isLoading }: DataGridProps<TData>) => {
  const defaultColDef = useMemo<ColDef>(
    () => ({
      sortable: true,
      filter: true,
      resizable: true,
      flex: 1,
    }),
    [],
  );

  return (
    <div className={`ag-theme-alpine ${styles.gridContainer} relative`} style={{ height: '500px', width: '100%' }}>
      {isLoading && <GridLoadingOverlay />}
      <AgGridReact
        rowData={rowData}
        columnDefs={columnDefs}
        defaultColDef={defaultColDef}
        pagination={false} // 커스텀 페이지네이션 사용을 위해 비활성화
        {...gridOptions}
      />
    </div>
  );
};
