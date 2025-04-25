import React from "react";
import { Link } from "react-router-dom";

const RiderHome = () => {


    return (
        <div>
            <button ><Link to="/login">Logout</Link></button>
            <h1>This is the rider home page </h1>
  
        </div>
    );
};

export default RiderHome;
