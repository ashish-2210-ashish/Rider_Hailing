import React from "react";
import { Link } from "react-router-dom";
import Sidebar from "./SideBar";


function getCookie(name: string): string | null {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift() || null;
  return null;
}

const Home: React.FC = () => {
  const token = getCookie("Token");
  const role = getCookie("Role");

  return (
    <div>
      <Sidebar/>
      {/* <button>
        <Link to="/login">Logout</Link>
      </button> */}

      {/* <p>This is the Admin home page.</p>
      <p><strong>Token:</strong> {token || "No token available"}</p>
      <p><strong>Role:</strong> {role || "No role available"}</p> */}
    </div>
  );
};

export default Home;
