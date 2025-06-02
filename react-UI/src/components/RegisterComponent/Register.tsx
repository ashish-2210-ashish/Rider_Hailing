import React, { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { FaEyeSlash, FaEye } from "react-icons/fa";
import './Register.scss';

const Register: React.FC = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    role: "RIDER",
    showPassword: false
  });

  const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = event.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const { username, password, role } = formData;

    if (!username || !password) {
      alert("Username and password cannot be empty !!!");
      return;
    }

    try {
      const response = await axios.post("http://localhost:8080/user/register", {
        username,
        password,
        role
      });
      alert(response.data);
      navigate("/login");
    } catch (e: any) {
      console.error(e);
      alert(e.response?.data || "Registration failed");
    }
  };

  const togglePasswordVisibility = () => {
    setFormData(prev => ({ ...prev, showPassword: !prev.showPassword }));
  };

  return (
    <div id='register-container'>
      <form onSubmit={handleSubmit}>
        <h1>Register</h1>

        <input
          name="username"
          placeholder="Enter username"
          type="email"
          value={formData.username}
          onChange={handleChange}
        />

        <div className="password-wrapper">
          <input
            name="password"
            placeholder="Enter password"
            type={formData.showPassword ? "text" : "password"}
            value={formData.password}
            onChange={handleChange}
          />
          <span className="eye-icon" onClick={togglePasswordVisibility}>
            {formData.showPassword ? <FaEyeSlash /> : <FaEye />}
          </span>
        </div>

        <select name="role" value={formData.role} onChange={handleChange}>
          <option value="RIDER">RIDER</option>
          <option value="DRIVER">DRIVER</option>
        </select>

        <button type="submit">Register</button>

        <p>
          Already registered? <Link to="/login">Login here.</Link>
        </p>
      </form>
    </div>
  );
};

export default Register;
