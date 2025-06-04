import React, { useEffect, useState } from "react";
import axios from "axios";
import Sidebar from "./SideBar";

interface Driver {
  driverId: number;
  coordinateX: number;
  coordinateY: number;
  available: boolean;
  user?: {
    id: number;
    username: string;
    role: string;
  } | null;
}

const AdminDriver: React.FC = () => {
  const [drivers, setDrivers] = useState<Driver[]>([]);

  // Get cookie by name
  const getCookie = (name: string): string | null => {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? match[2] : null;
  };

  useEffect(() => {
    const fetchDrivers = async () => {
      const token = getCookie("Token");
      if (!token) {
        alert("Token not found. Please login.");
        return;
      }

      try {
        const response = await axios.get("http://localhost:8080/driver", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setDrivers(response.data);
      } catch (error) {
        console.error("Error fetching drivers:", error);
        alert("Failed to fetch driver data.");
      }
    };

    fetchDrivers();
  }, []);

  return (
    <div className="d-flex">
      <Sidebar />
      <div className="container mt-4 ms-5">
        <h2>Welcome to the Admin Driver Page</h2>
        <div className="table-responsive mt-3">
          <table className="table table-dark table-striped">
            <thead>
              <tr>
                <th>Driver ID</th>
                <th>Username</th>
                <th>Role</th>
                <th>Coordinate X</th>
                <th>Coordinate Y</th>
                <th>Available</th>
              </tr>
            </thead>
            <tbody>
              {drivers.map((driver) => (
                <tr key={driver.driverId}>
                  <td>{driver.driverId}</td>
                  <td>{driver.user?.username || "N/A"}</td>
                  <td>{driver.user?.role || "N/A"}</td>
                  <td>{driver.coordinateX}</td>
                  <td>{driver.coordinateY}</td>
                  <td>{driver.available ? "Yes" : "No"}</td>
                </tr>
              ))}
              {drivers.length === 0 && (
                <tr>
                  <td colSpan={6} className="text-center">No drivers found.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AdminDriver;
