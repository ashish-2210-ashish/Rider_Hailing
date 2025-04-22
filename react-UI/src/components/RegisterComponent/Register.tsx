import React ,{ Component } from "react"
import axios from "axios";
import { Link } from "react-router-dom";
import './Register.scss'

type RegisterState={
  username :string;
  password :string;
  role :string;
}


class Register extends Component<{},RegisterState>{

constructor(){
  super();
  this.state={
    username:"",
    password:"",
    role:"RIDER"
  }

}

handelsubmit=async(event : React.FormEvent)=>{
  event.preventDefault();
  const {username,password}=this.state;

  if(!username || !password){
    alert("username nad password cannot be empty !!!")
    return;
  }
  try{
    const response=await axios.post("http://localhost:8080/user/register",this.state);
    console.log(response)
    alert(response.data);
  }
  catch(e){
    alert(response.data)
    console.error(e);
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

     <input name="password"
      placeholder="enter password"
      type="password"
      value={this.state.password}
      onChange={this.handelchange}
      />

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
