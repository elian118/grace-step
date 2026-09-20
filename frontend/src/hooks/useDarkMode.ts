import { useState, useEffect } from 'react';

export const useDarkMode = (): boolean => {
  const [isDark, setIsDark] = useState<boolean>(false);

  useEffect(() => {
    const checkDarkMode = () => {
      const html = document.documentElement;
      const body = document.body;

      // 1. HTML 또는 Body의 data-theme / class 감지
      const theme = html.getAttribute('data-theme') || body.getAttribute('data-theme');
      const hasDarkClass = html.classList.contains('dark') || body.classList.contains('dark');

      // 2. OS/시스템 다크모드 감지 (prefers-color-scheme)
      const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

      // theme 속성이 explicitly 설정된 경우 우선 적용, 없으면 시스템 설정이나 class 검사
      if (theme === 'dark') {
        setIsDark(true);
      } else if (theme === 'light') {
        setIsDark(false);
      } else {
        setIsDark(hasDarkClass || systemPrefersDark);
      }
    };

    checkDarkMode();

    // DOM 변화 감지 (data-theme, class 변경 시)
    const observer = new MutationObserver(checkDarkMode);
    observer.observe(document.documentElement, {
      attributes: true,
      attributeFilter: ['data-theme', 'class'],
    });
    observer.observe(document.body, {
      attributes: true,
      attributeFilter: ['data-theme', 'class'],
    });

    // 시스템 다크모드 변경 감지
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    const handleSystemChange = () => checkDarkMode();
    mediaQuery.addEventListener('change', handleSystemChange);

    return () => {
      observer.disconnect();
      mediaQuery.removeEventListener('change', handleSystemChange);
    };
  }, []);

  return isDark;
};
