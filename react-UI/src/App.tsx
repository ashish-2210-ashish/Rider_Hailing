import { Component } from "react";
import Register from "./components/RegisterComponent/Register";
import Home from "./components/HomeComponent/AdminHome/Home";
import Login from "./components/LoginComponent/Login";
import AdminDriver from "./components/HomeComponent/AdminHome/AdminDriver";
import AdminRider from "./components/HomeComponent/AdminHome/AdminRider";
import AdminAPI from "./components/HomeComponent/AdminHome/AdminAPI";
import AdminRide from "./components/HomeComponent/AdminHome/AdminRide";
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
          <Route path="/driver" element={< AdminDriver/>} />
          <Route path="/rider" element={< AdminRider/>} />
          <Route path="/api" element={< AdminAPI/>} />
          <Route path="/ride" element={< AdminRide/>} />
        </Routes>
      </div>
      </Router>
     
    );
  }
}

export default App;
