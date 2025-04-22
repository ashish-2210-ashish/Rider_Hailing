import React from "react";
import Login from "./Login";
import { useNavigate } from "react-router-dom";

const LoginWrapper = () =>{
    const navigate = useNavigate();
    return < Login navigate={navigate}/>
}

export default LoginWrapper;