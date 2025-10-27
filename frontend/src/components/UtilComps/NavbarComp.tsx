import { CassetteTape, Menu, X } from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import { useState } from "react";

export default function NavbarComp() {
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation();
  
  const getNavMode = () => {
    if (location.pathname === '/') return 'landing';
    if (location.pathname === '/auth') return 'auth';
    if (location.pathname === '/user-page') return 'user-page';
    if (location.pathname === '/search-albums') return 'search-albums';
  };
  
  const mode = getNavMode();
  const textColor = mode === 'auth' ? 'text-black' : 'text-white';
  const fixNav = mode === 'search-albums' ? '' : 'fixed';

  return (
    <nav className={`${fixNav} top-0 left-0 right-0 z-50 bg-transparent`}>
      <div className="max-w-7xl mx-auto px-4 md:px-6 lg:px-8">
        <div className="flex items-center justify-between h-20">
          {/*Logo*/}
          <Link to="/" className="flex items-center gap-3 no-underline">
            <svg width="0" height="0">
              <defs>
                <linearGradient id="myGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" stopColor="#9333ea" />
                  <stop offset="100%" stopColor="#ec4899" />
                </linearGradient>
              </defs>
            </svg>
            <CassetteTape size={40} stroke="url(#myGradient)" />
            <span className={`text-2xl font-bold ${textColor}`}>
              tapelog
            </span>
          </Link>

          {/*Desktop Navigation*/}
          <div className="hidden lg:flex items-center">
            {mode === 'user-page' && (
              <div className="flex items-center gap-6">
                <Link to="#home" className={`${textColor} hover:opacity-80 transition-opacity no-underline`}>
                  Home
                </Link>
                <Link to="#link" className={`${textColor} hover:opacity-80 transition-opacity no-underline`}>
                  Link
                </Link>
                <div className="relative group">
                  <button className={`${textColor} hover:opacity-80 transition-opacity`}>
                    Dropdown
                  </button>
                  <div className="absolute right-0 mt-2 w-48 bg-gray-900 rounded-lg shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all">
                    <Link to="#action/3.1" className="block px-4 py-2 text-white hover:bg-gray-800 no-underline">
                      Action
                    </Link>
                    <Link to="#action/3.2" className="block px-4 py-2 text-white hover:bg-gray-800 no-underline">
                      Another action
                    </Link>
                    <Link to="#action/3.3" className="block px-4 py-2 text-white hover:bg-gray-800 no-underline">
                      Something
                    </Link>
                    <div className="border-t border-gray-700"></div>
                    <Link to="#action/3.4" className="block px-4 py-2 text-white hover:bg-gray-800 no-underline">
                      Separated link
                    </Link>
                  </div>
                </div>
              </div>
            )}
            
            {mode === 'landing' && (
              <div className="flex items-center gap-3">
                <Link to='/auth?mode=sign-in'>
                  <button className="px-6 py-2 border border-gray-700 text-white rounded-lg hover:border-purple-600 hover:bg-purple-600 hover:bg-opacity-10 transition-all">
                    Sign In
                  </button>
                </Link>
                <Link to='/auth?mode=sign-up'>
                  <button className="px-6 py-2 bg-gradient-to-r from-purple-600 to-pink-600 text-white rounded-lg font-semibold hover:opacity-90 transition-opacity">
                    Sign Up
                  </button>
                </Link>
              </div>
            )}
          </div>

          {/*Mobile Menu Button*/}
          <button
            onClick={() => setIsOpen(!isOpen)}
            className={`lg:hidden ${textColor}`}
          >
            {isOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>

        {/*Mobile Navigation*/}
        {isOpen && (
          <div className="lg:hidden pb-4">
            {mode === 'user-page' && (
              <div className="flex flex-col gap-4">
                <Link to="#home" className={`${textColor} hover:opacity-80 transition-opacity no-underline`}>
                  Home
                </Link>
                <Link to="#link" className={`${textColor} hover:opacity-80 transition-opacity no-underline`}>
                  Link
                </Link>
                <div className="flex flex-col gap-2 pl-4">
                  <span className={`${textColor} font-semibold`}>Dropdown</span>
                  <Link to="#action/3.1" className={`${textColor} text-sm hover:opacity-80 no-underline`}>
                    Action
                  </Link>
                  <Link to="#action/3.2" className={`${textColor} text-sm hover:opacity-80 no-underline`}>
                    Another action
                  </Link>
                  <Link to="#action/3.3" className={`${textColor} text-sm hover:opacity-80 no-underline`}>
                    Something
                  </Link>
                  <Link to="#action/3.4" className={`${textColor} text-sm hover:opacity-80 no-underline`}>
                    Separated link
                  </Link>
                </div>
              </div>
            )}
            
            {mode === 'landing' && (
              <div className="flex flex-col gap-3">
                <Link to='/auth?mode=sign-in'>
                  <button className="w-full px-6 py-2 border border-gray-700 text-white rounded-lg hover:border-purple-600 hover:bg-purple-600 hover:bg-opacity-10 transition-all">
                    Sign In
                  </button>
                </Link>
                <Link to='/auth?mode=sign-up'>
                  <button className="w-full px-6 py-2 bg-gradient-to-r from-purple-600 to-pink-600 text-white rounded-lg font-semibold hover:opacity-90 transition-opacity">
                    Sign Up
                  </button>
                </Link>
              </div>
            )}
          </div>
        )}
      </div>
    </nav>
  );
}
