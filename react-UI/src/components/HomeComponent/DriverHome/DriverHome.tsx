import React from "react";
import { Link } from "react-router-dom";

const DriverHome = () => {


    return (
        <div>
            <button ><Link to="/login">Logout</Link></button>
            <h1>This is the driver home page </h1>
    
        </div>
    );
};

export default DriverHome;
