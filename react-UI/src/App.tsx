import { Component } from "react";
import Register from "./components/RegisterComponent/Register";
import Login from "./components/LoginComponent/Login";
import Home from "./components/HomeComponent/Home";
import { BrowserRouter as Router,Route ,Routes , Navigate} from "react-router-dom";
import './App.scss'

class App extends Component {
  render() {
    return (
      <Router>
         <div id='application-container'>
        <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={< Login/>} />
          <Route path="/register" element={< Register/>} />
          <Route path="/home" element={< Home/>} />
        </Routes>
      </div>
      </Router>
     
    );
  }
}

export default App;
