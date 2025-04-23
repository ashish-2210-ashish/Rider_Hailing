import React from "react";
import Register from "./Register";
import { useNavigate } from "react-router";

const RegisterWrapper= () => {
    const navigate = useNavigate();
    return <Register navigate={navigate}/>
}

export default RegisterWrapper;




