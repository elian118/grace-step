import React, { useMemo } from 'react';
import { AgGridReact } from 'ag-grid-react';
import type { ColDef, GridOptions } from 'ag-grid-community';
import styles from '@/styles/grid/DataGrid.module.css';
import { GridLoadingOverlay } from './components/loading/GridLoadingOverlay';

import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';

interface DataGridProps<TData> {
  rowData: TData[];
  columnDefs: ColDef<TData>[];
  gridOptions?: GridOptions<TData>;
  isLoading?: boolean;
}

export const DataGrid = <TData,>({ rowData, columnDefs, gridOptions, isLoading }: DataGridProps<TData>) => {
  const defaultColDef = useMemo<ColDef<TData>>(
    () => ({
      sortable: true,
      filter: true,
      resizable: true,
      flex: 1,
      cellClass: 'flex items-center', // 셀 내부 버티컬 얼라인 확보
    }),
    [],
  );

  return (
    /* 모서리 둥글기(rounded-xl)와 테두리를 잡아주는 래퍼 */
    <div className={`${styles.gridWrapper} relative`} style={{ height: '500px', width: '100%' }}>
      {isLoading && <GridLoadingOverlay />}
      <div className={`ag-theme-alpine ${styles.gridContainer}`}>
        <AgGridReact<TData>
          rowData={rowData}
          columnDefs={columnDefs}
          defaultColDef={defaultColDef}
          pagination={false}
          singleClickEdit={true}
          rowSelection="single"
          animateRows={true}
          {...gridOptions}
        />
      </div>
    </div>
  );
};
