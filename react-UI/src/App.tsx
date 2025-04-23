import { Component } from "react";
import RegisterWrapper from "./components/RegisterComponent/RegisterWrapper";
import Home from "./components/HomeComponent/Home";
import LoginWrapper from "./components/LoginComponent/LoginWrapper";
import DriverHome from "./components/HomeComponent/DriverHome/DriverHome";
import RiderHome from "./components/HomeComponent/RiderHome/RiderHome";
import { BrowserRouter as Router,Route ,Routes , Navigate} from "react-router-dom";
import './App.scss'

class App extends Component {
  render() {
    return (
      <Router>
         <div id='application-container'>
        <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={< LoginWrapper/>} />
          <Route path="/register" element={< RegisterWrapper/>} />
          <Route path="/home" element={< Home/>} />
          <Route path="/driverHome" element={< DriverHome/>} />
          <Route path="/riderHome" element={< RiderHome/>} />
        </Routes>
      </div>
      </Router>
     
    );
  }
}

export default App;
