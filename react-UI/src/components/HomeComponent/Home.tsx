import React from "react";
import { Link } from "react-router-dom";

const Home = () => {
    // Retrieve and parse session data from sessionStorage
    const session = sessionStorage.getItem("session");
    const userDetails=sessionStorage.getItem("userDetails")
    const token = session ? JSON.parse(session).token : "No token available";
    const username=userDetails ? JSON.parse(userDetails).sub: "unknown";
    const role = userDetails ? JSON.parse(userDetails).role : "unknown";

    return (
        <div>
            <button onClick={() => {
                sessionStorage.removeItem("session");
            }}><Link to="/login">Logout</Link></button>
            <p>This is the home page and your token is: {token}</p>
            <p>user name : {username}</p>
            <p>role : {role}</p>
        </div>
    );
};

export default Home;
