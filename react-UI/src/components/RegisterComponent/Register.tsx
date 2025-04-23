import React ,{ Component } from "react"
import axios from "axios";
import { Link } from "react-router-dom";
import { FaEyeSlash,FaEye } from "react-icons/fa";
import './Register.scss'

type RegisterState={
  username :string;
  password :string;
  showpassword : boolean;
  role :string;
}

type RegisterProps={
  navigate : (path : string) => void;
}

class Register extends Component<RegisterProps,RegisterState>{

constructor(props : RegisterProps){
  super(props);
  this.state={
    username:"",
    password:"",
    role:"RIDER",
    showpassword:false
  }

}

handelsubmit=async(event : React.FormEvent)=>{
  event.preventDefault();
  const {username,password}=this.state;

  if(!username || !password){
    alert("username nad password cannot be empty !!!")
    return;
  }
  try {
    const response = await axios.post("http://localhost:8080/user/register", this.state);
    console.log(response);
    alert(response.data);
    this.props.navigate("/login");
  } catch (e: any) {
    console.error(e);
    alert(e.response?.data || "Registration failed");
  }
}

handelchange=(event:any)=>{
  this.setState({[event.target.name] : event.target.value})

}

render(){
  return(
    <div id='register-container'>
       <form onSubmit={this.handelsubmit}>
      <h1>Register</h1>
      <input name="username"
      placeholder="enter username"
      type="email"
      value={this.state.username}
      onChange={this.handelchange}
      />

    <div className="password-wrapper">
      <input name="password"
        placeholder="enter password"
        type={this.state.showpassword?"text":"password"}
        value={this.state.password}
        onChange={this.handelchange}
      />
      <span 
      className="eye-icon"
      onClick={()=>{this.setState({showpassword:!this.state.showpassword})}}>
        {this.state.showpassword? <FaEyeSlash/> : <FaEye/>}
      </span>
    </div>
    <select name="role"
     value={this.state.role}
     onChange={this.handelchange}>
          <option value="RIDER">RIDER</option>
          <option value="DRIVER">DRIVER</option>
    </select>

    <button type="submit">Register</button>

    <p>Already registered? <Link to ="/login">login here.</Link> </p>
     
     
    </form>
    </div>
    
  )}
}


export default Register
