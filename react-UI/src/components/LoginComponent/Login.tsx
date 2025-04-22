import React,{Component} from "react";
import axios from "axios";
import { Link,Navigate,useNavigate } from "react-router-dom";
import './Login.scss'

type LoginState={
    username : string;
    password : string; 
}

type LoginProps={
    navigate : (path:string) => void;
}

class Login extends Component<LoginProps,LoginState>{

    constructor(props : LoginProps){
        super(props);

        this.state={
            username:"",
            password:""
        }
    }

    handlesubmit=async (event :React.FormEvent)=>{
        event.preventDefault();
        const {username,password}=this.state;

        if(!username || !password){
          alert("username nad password cannot be empty !!!")
          return;
        }

        try{
            const response = await axios.post("http://localhost:8080/user/login",this.state);
            alert('successfully logined ...');
            const jwt_token=response.data.token;
            console.log('token = ',jwt_token)

            const expiry_time= new Date().getTime()+ 60 * 60 * 1000;

            const sessionData={
                token:jwt_token,
                expiry:expiry_time
            }

            sessionStorage.setItem("session",JSON.stringify(sessionData));
            this.props.navigate("/home");
            
        }
        catch(e){
            console.error(e);
            alert('logined failed ...\n(invalid username or password)')
        }
    }

    handlechange=(event : any)=>{
        this.setState({[event.target.name] : event.target.value})
    }

    render(){
        return(
            <div id='login-container'>
                <form onSubmit={this.handlesubmit}>
                <h1>Login</h1>
                <input name="username"
                placeholder="enter username"
                type="email"
                value={this.state.username}
                onChange={this.handlechange}
                />

                <input name="password"
                placeholder="enter password"
                type="password"
                value={this.state.password}
                onChange={this.handlechange}
                />

                <button type="submit"> Login</button>

                <p>Don't hava an account?<Link to ="/register"> register here </Link></p>
                </form>
            </div>
        )

    }
}


export default Login