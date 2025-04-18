import React ,{ Component } from "react"
import axios from "axios";

type RegisterState={
  username :string;
  password :string;
  role :string;
}


class App extends Component<{},RegisterState>{

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
  try{
    await axios.post("http://localhost:8080/user/register",this.state);
    alert("Registered successfully ...");
  }
  catch(e){
    alert("Registered failed ...")
    console.error(e);
  }
}

handelchange=(event:any)=>{
  this.setState({[event.target.name] : event.target.value})

}

render(){
  return(
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

    <select name="role" value={this.state.role} onChange={this.handleChange}>
          <option value="RIDER">RIDER</option>
          <option value="ADMIN">ADMIN</option>
        </select>

        <button type="submit">Register</button>
     
     
    </form>
    
  )}
}


export default App
