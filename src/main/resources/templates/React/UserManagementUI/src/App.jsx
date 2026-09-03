import {Router} from "react-router-dom";
import Navbar from './components/Navbar';
import Mainpage from './pages/mainpage';
import Login from './pages/login';
import Signup from './pages/signup';
import ProtectedRoute from './components/ProtectedRoute';
import Dashboard from './pages/dashboard';
function App(){
    return(
        <Router>
            <div className="app">
                <Navbar/>
                <Routes>
                    <Route path="/" element={<Mainpage/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/dashboard"
                     element={
                     <ProtectedRoute>
                        <Dashboard/>
                     </ProtectedRoute>
                     }/>
                </Routes>
            </div>




        </Router>
    )
}

export default App;