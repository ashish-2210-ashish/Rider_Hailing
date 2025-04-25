import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const Home = () => {
    const [username, setUsername] = useState(""); // State to store username
    const [loading, setLoading] = useState(true); // Loading state

    useEffect(() => {
        // Fetch the username when the component mounts
        const fetchUsername = async () => {
            try {
                const response = await axios.get("http://localhost:8080/user/profile");
                setUsername(response.data.username); // Assuming the response contains a username field
                setLoading(false); // Set loading to false after the request is complete
            } catch (error) {
                console.error("Error fetching username:", error);
                setLoading(false); // Even if there's an error, stop loading
            }
        };

        fetchUsername();
    }, []); // Empty dependency array means this effect runs once when the component mounts

    // Display a loading message while waiting for the API response
    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <button
                onClick={() => {
                    sessionStorage.removeItem("session"); // Clear session storage
                }}
            >
                <Link to="/login">Logout</Link>
            </button>
            <h1>This is the home page</h1>
            <p>Hello, {username}</p>
        </div>
    );
};

export default Home;
