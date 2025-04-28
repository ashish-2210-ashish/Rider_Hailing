import React from "react";
import { Link } from "react-router-dom";

// Helper to get cookie value
function getCookie(name: string): string | null {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(';').shift() || null;
  return null;
}

const Home = () => {
  const token = getCookie("Token");
  const role = getCookie("Role");

  return (
    <div>
      <button>
        <Link to="/login">Logout</Link>
      </button>

      <p>This is the home page.</p>
      <p><strong>Token:</strong> {token || "No token available"}</p>
      <p><strong>Role:</strong> {role || "No role available"}</p>
    </div>
  );
};

export default Home;
