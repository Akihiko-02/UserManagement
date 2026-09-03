import axios from 'axios';

const BASE_URL = "http://localhost:8080/api";

const api = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
});


const authService = {

    signupNormalUser: async (username, email, password) => {
        try {
            const response = await api.post(
                '/auth/registernormaluser',
                { username, email, password }
            );

            return response.data;

        } catch (error) {
            console.error("SignUp failed", error);
            throw error;
        }
    },


    login: async (username, password) => {
        try {
            const response = await api.post(
                '/auth/login',
                { username, password }
            );

            const user = await authService.fetchCurrentUser();

            return {
                ...response.data,
                user
            };

        } catch (error) {
            console.error("Login error", error);
            throw error;
        }
    },


    fetchCurrentUser: async () => {
        try {
            const response = await api.get('/auth/fetchCurrentUser');

            localStorage.setItem(
                'user',
                JSON.stringify(response.data)
            );

            return response.data;

        } catch (error) {
            console.error("Error fetching user data", error);

            if (error.response && error.response.status === 401) {
                await authService.logout();
            }

            return null;
        }
    },


    getCurrentUser: () => {
        const user = localStorage.getItem('user');

        try {
            return user ? JSON.parse(user) : null;

        } catch (error) {
            console.error('Error parsing user data', error);
            return null;
        }
    },


    logout: async () => {
        try {
            await api.post('/auth/logout');
            localStorage.removeItem('user');

        } catch (error) {
            console.error("Logout failed", error);
            localStorage.removeItem('user');
        }
    },


    isAuthenticated: async () => {
        try {
            const user = await authService.fetchCurrentUser();
            return !!user;

        } catch (error) {
            return false;
        }
    },

    updateProfile: async(userData)=>{
        try{
            const response = await api.put(`/users/updateuser/${userData.id}`,userData);
            const currentUser = authService.getCurrentUser();
            const updateUser = {...currentUser,...response.data};
            localStorage.setItem('user',JSON.stringify(updateUser));
        }
        catch(error){
            console.error("profile update failed",error);
            throw error;
        }
    },
    getAllUsers: async()=>{
        try{
            const response = await api.get('/users/getallusers');
            return response.data;
        }
        catch(error){
            console.error("Failed to fetch all users",error);
            throw error;
        }
    },
    deleteUser: async(userId)=>{
        try{
            const response = await api.delete(`/users/deleteuser/${userId}`);
            return response.data;
        }
        catch(error){
            console.error("Failed to delete user",error);
            throw error;
        }
    },
    changePassword: async(currentPassword, newPassword, confirmPassword)=>{
        try{
            const currentUser = authService.getCurrentUser();
            if(!currentUser || !currentUser.id){
                throw new Error('User not found');
            }
            const response = await api.put(`/users/changepassword/${currentUser.id}`, {
                currentPassword,
                newPassword,
                confirmPassword
            });
            return response.data;
        }
        catch(error){
            console.error("Failed to change the password", error);
            throw error;
        }
    }
};


api.interceptors.response.use(
    (response) => response,

    async (error) => {

        if (error.response) {

            switch (error.response.status) {

                case 401:
                    console.error("Unauthorized");
                    localStorage.removeItem('user');
                    window.location.href = '/login';
                    break;

                case 403:
                    console.error("Access Forbidden");
                    break;

                case 404:
                    console.error("Resource not found");
                    break;

                case 500:
                    console.error("Internal server error");
                    break;

                default:
                    console.error("Request failed:", error.response.status);
            }

        } else if (error.request) {

            console.error("No response received", error.request);

        } else {

            console.error(
                "Error in setting up request",
                error.message
            );
        }

        return Promise.reject(error);
    }
);


export default authService;
export {api, authService};