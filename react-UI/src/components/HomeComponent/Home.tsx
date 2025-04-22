import React from "react";
import { Link } from "react-router-dom";

const Home = () => {
    // Retrieve and parse session data from sessionStorage
    const session = sessionStorage.getItem("session");
    const token = session ? JSON.parse(session).token : "No token available";

    return (
        <div>
            <button onClick={() => {
                sessionStorage.removeItem("session");
            }}><Link to="/login">Logout</Link></button>
            <p>This is the home page and your token is: {token}</p>
        </div>
    );
};

export default Home;
