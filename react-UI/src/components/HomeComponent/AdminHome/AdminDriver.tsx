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
  const [newDriver, setNewDriver] = useState({ coordinateX: 0, coordinateY: 0 });
  const [editDriverId, setEditDriverId] = useState<number | null>(null);
  const [editCoordinates, setEditCoordinates] = useState({ coordinateX: 0, coordinateY: 0 });

  const getCookie = (name: string): string | null => {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? match[2] : null;
  };

  const fetchDrivers = async () => {
    const token = getCookie("Token");
    if (!token) return alert("Token not found. Please login.");
    try {
      const response = await axios.get("http://localhost:8080/driver", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDrivers(response.data);
    } catch (error) {
      console.error("Error fetching drivers:", error);
      alert("Failed to fetch driver data.");
    }
  };

  useEffect(() => {
    fetchDrivers();
  }, []);

  const addDriver = async () => {
    const token = getCookie("Token");
    if (!token) return alert("Token not found. Please login.");
    try {
      await axios.post(
        "http://localhost:8080/driver",
        newDriver,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setNewDriver({ coordinateX: 0, coordinateY: 0 });
      fetchDrivers();
    } catch (err) {
      console.error("Add error:", err);
      alert("Failed to add driver.");
    }
  };

  const updateDriver = async () => {
    const token = getCookie("Token");
    if (!token || editDriverId === null) return alert("Token or driver ID missing.");
    try {
      await axios.put(
        `http://localhost:8080/driver/${editDriverId}`,
        editCoordinates,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setEditDriverId(null);
      fetchDrivers();
    } catch (err) {
      console.error("Update error:", err);
      alert("Failed to update driver.");
    }
  };

  const deleteDriver = async (driverId: number) => {
    const token = getCookie("Token");
    if (!token) return alert("Token not found. Please login.");
    try {
      await axios.delete(`http://localhost:8080/driver/${driverId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchDrivers();
    } catch (err) {
      console.error("Delete error:", err);
      alert("Failed to delete driver.");
    }
  };

  return (
    <div className="d-flex">
  
      <div className="container mt-4 ms-5">
        <h2>Admin Driver Dashboard</h2>

        {/* Add Driver Form */}
        <div className="my-4">
          <h4>Add New Driver</h4>
          <input
            type="number"
            placeholder="Coordinate X"
            value={newDriver.coordinateX}
            onChange={(e) => setNewDriver({ ...newDriver, coordinateX: parseFloat(e.target.value) })}
          />
          <input
            type="number"
            placeholder="Coordinate Y"
            value={newDriver.coordinateY}
            onChange={(e) => setNewDriver({ ...newDriver, coordinateY: parseFloat(e.target.value) })}
          />
          <button className="btn btn-success ms-2" onClick={addDriver}>Add</button>
        </div>

        {/* Driver Table */}
        <div className="table-responsive">
          <table className="table table-dark table-striped">
            <thead>
              <tr>
                <th>Driver ID</th>
                <th>Username</th>
                <th>Role</th>
                <th>Coordinate X</th>
                <th>Coordinate Y</th>
                <th>Available</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {drivers.map((driver) => (
                <tr key={driver.driverId}>
                  <td>{driver.driverId}</td>
                  <td>{driver.user?.username || "N/A"}</td>
                  <td>{driver.user?.role || "N/A"}</td>
                  <td>
                    {editDriverId === driver.driverId ? (
                      <input
                        type="number"
                        value={editCoordinates.coordinateX}
                        onChange={(e) =>
                          setEditCoordinates({ ...editCoordinates, coordinateX: parseFloat(e.target.value) })
                        }
                      />
                    ) : (
                      driver.coordinateX
                    )}
                  </td>
                  <td>
                    {editDriverId === driver.driverId ? (
                      <input
                        type="number"
                        value={editCoordinates.coordinateY}
                        onChange={(e) =>
                          setEditCoordinates({ ...editCoordinates, coordinateY: parseFloat(e.target.value) })
                        }
                      />
                    ) : (
                      driver.coordinateY
                    )}
                  </td>
                  <td>{driver.available ? "Yes" : "No"}</td>
                  <td>
                    {editDriverId === driver.driverId ? (
                      <>
                        <button className="btn btn-primary btn-sm me-1" onClick={updateDriver}>
                          Save
                        </button>
                        <button className="btn btn-secondary btn-sm" onClick={() => setEditDriverId(null)}>
                          Cancel
                        </button>
                      </>
                    ) : (
                      <>
                        <button
                          className="btn btn-warning btn-sm me-1"
                          onClick={() => {
                            setEditDriverId(driver.driverId);
                            setEditCoordinates({
                              coordinateX: driver.coordinateX,
                              coordinateY: driver.coordinateY,
                            });
                          }}
                        >
                          Edit
                        </button>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => deleteDriver(driver.driverId)}
                        >
                          Delete
                        </button>
                      </>
                    )}
                  </td>
                </tr>
              ))}
              {drivers.length === 0 && (
                <tr>
                  <td colSpan={7} className="text-center">No drivers found.</td>
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
