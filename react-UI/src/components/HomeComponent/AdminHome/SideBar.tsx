import React from 'react';
import { Link } from 'react-router-dom';
import './SideBar.css'

const Sidebar: React.FC = () => {
  return (
    <div className="sidebar">
      <Link to="/home">Home</Link>
      <Link to="/rider">Rider</Link>
      <Link to="/driver">Driver</Link>
      <button>
        <Link to="/login">Logout</Link>
      </button>
    </div>
  );
};

export default Sidebar;
