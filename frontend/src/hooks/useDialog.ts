import { useAppDispatch, useAppSelector } from '@/store';
import { openDialog, closeDialog, closeAllDialogs, type DialogInstance, type DialogOptions } from '@/store/dialogSlice';
import { useCallback } from 'react';

export interface OpenDialogParams<TProps = never> {
  title?: string;
  props?: TProps;
  options?: DialogOptions;
}

export const useDialog = () => {
  const dispatch = useAppDispatch();
  const activeDialogs = useAppSelector((state) => state.dialog.activeDialogs);

  const open = useCallback(
    <TProps extends undefined = never>(key: string, params?: OpenDialogParams<TProps>) => {
      dispatch(
        openDialog({
          key,
          title: params?.title,
          props: params?.props,
          options: params?.options,
        }),
      );
    },
    [dispatch],
  );

  const closeByKey = useCallback(
    (key: string) => {
      dispatch(closeDialog(key));
    },
    [dispatch]
  );

  const closeAll = useCallback(() => {
    dispatch(closeAllDialogs());
  }, [dispatch]);

  const isOpen = useCallback(
    (key: string) => {
      return activeDialogs.some((d) => d.key === key);
    },
    [activeDialogs]
  );

  return {
    activeDialogs,
    open,
    closeByKey,
    closeAll,
    isOpen,
  };
};
export type { DialogOptions, DialogInstance };
