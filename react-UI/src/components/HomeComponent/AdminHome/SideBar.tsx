import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './SideBar.css';

const Sidebar: React.FC = () => {
  const [isOpen, setIsOpen] = useState(true);

  const toggleSidebar = () => setIsOpen(!isOpen);

  return (
    <>
      <button className="toggle-button" onClick={toggleSidebar}>
        {isOpen ? '⬅' : '➡'}
      </button>
      <div className={`sidebar ${isOpen ? 'open' : 'closed'}`}>
        <Link to="/home">Home</Link>
        <Link to="/rider">Rider</Link>
        <Link to="/driver">Driver</Link>
        <Link to="/login" className="logout-link">Logout</Link>
      </div>
    </>
  );
};

export default Sidebar;
