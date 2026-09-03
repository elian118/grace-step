'use client';

import { useCallback, useEffect, useState } from 'react';

export const useAsync = <T>(
  asyncCallBack: () => Promise<T>,
  deps: any[] = [],
): [Error | null, () => void] => {
  const [error, setError] = useState<Error | null>(null);
  useEffect(() => {
    asyncCallBack().catch((e) => setError(e));
  }, deps);
  const resetError = useCallback(() => setError(() => null), []);
  return [error, resetError];
};
