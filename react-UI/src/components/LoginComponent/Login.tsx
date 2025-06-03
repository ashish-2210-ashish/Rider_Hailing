import React, {useState} from "react";
import  axios  from "axios";
import './Login.scss';
import { Link, useNavigate } from "react-router-dom";
import { FaEyeSlash, FaEye } from "react-icons/fa";

const Login : React.FC = () => {
    const navigate = useNavigate();
    const [username,setUsername] = useState<string>("");
    const [password,setPassword] = useState<string>("");
    const [showPassword,setShowPassword] = useState<boolean>(false);

    const handleSubmit = async (event : React.FormEvent) => {

        event.preventDefault();
        
        if (!username || !password){
            alert("Username and password cannot be empty!");
            return ;
        }

        try{
            const response= await axios.post(
            "http://localhost:8080/user/login",
            {username,password},
            { withCredentials: true }
        );
        alert("Successfully logged in ....");
        navigate("/home");
        }catch(e){
            console.error(e);
            alert("Login failed...\n(Invalid username or password)");

        }


    }

    return (
        <div id="login-container">
            <form onSubmit={handleSubmit}>
                <h1>Login</h1>

                <input
                    name="username"
                    placeholder="Enter username"
                    type="email"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <div className="password-wrapper">
                    <input
                        name="password"
                        placeholder="Enter password"
                        type={showPassword ? "text" : "password"}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <span
                        className="eye-icon"
                        onClick={() => setShowPassword(!showPassword)}
                    >
                        {showPassword ? <FaEyeSlash /> : <FaEye />}
                    </span>
                </div>

                <button type="submit">Login</button>

                <p>
                    Don't have an account?{" "}
                    <Link to="/register">Register here</Link>
                </p>
            </form>
        </div>
    );
};

export default Login;























// import React, { useState } from "react";
// import axios from "axios";
// import { Link, useNavigate } from "react-router-dom";
// import { FaEyeSlash, FaEye } from "react-icons/fa";
// import './Login.scss';

// const Login: React.FC = () => {
//     const navigate = useNavigate();

//     // State variables
//     const [username, setUsername] = useState<string>("");
//     const [password, setPassword] = useState<string>("");
//     const [showPassword, setShowPassword] = useState<boolean>(false);

//     // Submit handler
//     const handleSubmit = async (event: React.FormEvent) => {
//         event.preventDefault();

//         if (!username || !password) {
//             alert("Username and password cannot be empty!!!");
//             return;
//         }

//         try {
//             const response = await axios.post(
//                 "http://localhost:8080/user/login",
//                 { username, password },
//                 { withCredentials: true }
//             );

//             alert("Successfully logged in...");
//             navigate("/home");
//         } catch (e) {
//             console.error(e);
//             alert("Login failed...\n(Invalid username or password)");
//         }
//     };

//     return (
//         <div id="login-container">
//             <form onSubmit={handleSubmit}>
//                 <h1>Login</h1>

//                 <input
//                     name="username"
//                     placeholder="Enter username"
//                     type="email"
//                     value={username}
//                     onChange={(e) => setUsername(e.target.value)}
//                 />

//                 <div className="password-wrapper">
//                     <input
//                         name="password"
//                         placeholder="Enter password"
//                         type={showPassword ? "text" : "password"}
//                         value={password}
//                         onChange={(e) => setPassword(e.target.value)}
//                     />
//                     <span
//                         className="eye-icon"
//                         onClick={() => setShowPassword(!showPassword)}
//                     >
//                         {showPassword ? <FaEyeSlash /> : <FaEye />}
//                     </span>
//                 </div>

//                 <button type="submit">Login</button>

//                 <p>
//                     Don't have an account?{" "}
//                     <Link to="/register">Register here</Link>
//                 </p>
//             </form>
//         </div>
//     );
// };

// export default Login;
