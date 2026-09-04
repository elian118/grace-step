import React from 'react';
import { menus } from '@/router/menus.tsx';
import { useNavigate } from 'react-router-dom';

const Header = () => {
  const navigate = useNavigate();
  const mainPagePaths = ['/attendance', '/classroom', '/exam', '/student'];

  return (
    <header className="navbar bg-base-100 border-b border-base-200 px-6 xl:rounded-t-box">
      <div className="flex-1">
        <span className="btn btn-ghost text-xl font-bold" onClick={() => navigate('/')}>
          Logo
        </span>
      </div>
      <div className="flex-none">
        <ul className="menu menu-horizontal px-1 gap-2">
          {menus
            .filter((e) => mainPagePaths.includes(e.path))
            .map((menu) => (
              <li key={`menu-${menu.path}`} onClick={() => navigate(menu.path)}>
                <span>{menu.label}</span>
              </li>
            ))}
        </ul>
      </div>
    </header>
  );
};

export default Header;
