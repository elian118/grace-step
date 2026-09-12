import React, { useMemo, useEffect, useState, forwardRef } from 'react';
import { AgGridReact } from 'ag-grid-react';
import type { ColDef, GridOptions } from 'ag-grid-community';
import styles from '@/styles/grid/DataGrid.module.css';
import { GridLoadingOverlay } from './components/loading/GridLoadingOverlay';

interface DataGridProps<TData> {
  rowData: TData[];
  columnDefs: ColDef<TData>[];
  gridOptions?: GridOptions<TData>;
  isLoading?: boolean;
}

// forwardRef 및 제네릭(Generic) 적용
export const DataGrid = forwardRef(function DataGrid<TData>(
  { rowData, columnDefs, gridOptions, isLoading }: DataGridProps<TData>,
  ref: React.ForwardedRef<AgGridReact<TData>>,
) {
  const [isDark, setIsDark] = useState(false);

  useEffect(() => {
    const checkDarkMode = () => {
      const html = document.documentElement;
      const theme = html.getAttribute('data-theme');
      const hasDarkClass = html.classList.contains('dark');
      setIsDark(theme === 'dark' || hasDarkClass);
    };

    checkDarkMode();

    const observer = new MutationObserver(checkDarkMode);
    observer.observe(document.documentElement, {
      attributes: true,
      attributeFilter: ['data-theme', 'class'],
    });

    return () => observer.disconnect();
  }, []);

  const defaultColDef = useMemo<ColDef<TData>>(
    () => ({
      sortable: true,
      filter: true,
      resizable: true,
      flex: 1,
      cellClass: 'flex items-center',
    }),
    [],
  );

  const themeClass = isDark ? 'ag-theme-quartz-dark' : 'ag-theme-quartz';

  return (
    <div className={`${styles.gridWrapper} relative`} style={{ height: '500px', width: '100%' }}>
      {isLoading && <GridLoadingOverlay />}
      <div className={`${themeClass} ag-theme-daisy ${styles.gridContainer}`}>
        <AgGridReact<TData>
          ref={ref} // <- AgGridReact에 ref 전달
          rowData={rowData}
          rowHeight={34}
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
}) as <TData>(props: DataGridProps<TData> & { ref?: React.ForwardedRef<AgGridReact<TData>> }) => React.ReactElement;
