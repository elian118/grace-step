'use client';

import { useState } from 'react';

export const useLoading = () => {
  const [loading, setLoading] = useState();

  return [loading, setLoading];
};
