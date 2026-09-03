import { useState } from "react"
import { useNavigate } from "react-router-dom";
import authService from "../services/authService";
import "../styles/login.css";

const login= ()=>{
    const[ username, setUsername] = useState('');
    const[password,setPassword] = useState('');
    const[error,setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e)=>{
        e.preventDefault();
        setError('');

        try{
            await authService.login(username,password);
            navigate('/');
        }
        catch(error){
            setError("Login failed. Please check your credentials.");
            console.error('Login error',error);
        }

    };

    return(
        <div className="login-container">
            <div className="login-form">
                <h2>Login</h2>
                {error&&(
                    <div className="error-message">
                        {error}
                    </div>
                )}
                <form action="" onSubmit={handleLogin}>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input type="text" id="username" value={username} onChange={(e)=>setUsername(e.target.value)} required/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input type="password" id="password" value={password} onChange={(e)=>setPassword(e.target.value)} required/>
                    </div>
                    <button type="submit" className="login-button">Login</button>
                    <p>
                        Don't have an account? <Link to="/signup">Sign up</Link>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default login;