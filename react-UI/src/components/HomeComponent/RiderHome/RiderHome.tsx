import React from "react";
import { Link } from "react-router-dom";

const RiderHome = () =>{
    const session = sessionStorage.getItem("session");
    const userDetails=sessionStorage.getItem("userDetails")
    const username=userDetails ? JSON.parse(userDetails).sub: "unknown";
    const role = userDetails ? JSON.parse(userDetails).role : "unknown";

    return (
        <div>
            <button onClick={() => {
                sessionStorage.removeItem("session");
            }}><Link to="/login">Logout</Link></button>
            <p>This is the RIDER home page </p>
            <p>user name : {username}</p>
            <p>role : {role}</p>
        </div>
    );
};
export default RiderHome;