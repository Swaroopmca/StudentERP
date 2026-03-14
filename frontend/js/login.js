// Login page specific JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Check if already logged in
    checkLoginStatus();
    
    // Add input validation
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    
    usernameInput.addEventListener('input', validateUsername);
    passwordInput.addEventListener('input', validatePassword);
});

function validateUsername() {
    const username = document.getElementById('username').value;
    const errorDiv = document.getElementById('username-error');
    
    if (username.length < 3) {
        errorDiv.textContent = 'Username must be at least 3 characters';
        return false;
    }
    errorDiv.textContent = '';
    return true;
}

function validatePassword() {
    const password = document.getElementById('password').value;
    const errorDiv = document.getElementById('password-error');
    
    if (password.length < 6) {
        errorDiv.textContent = 'Password must be at least 6 characters';
        return false;
    }
    errorDiv.textContent = '';
    return true;
}

async function checkLoginStatus() {
    try {
        const response = await apiCall('/auth/me', 'GET');
        if (response) {
            // Already logged in, redirect to appropriate dashboard
            const user = await apiCall('/auth/me');
            if (user.role === 'ADMIN') window.location.href = 'admin/dashboard.html';
            else if (user.role === 'TEACHER') window.location.href = 'teacher/dashboard.html';
            else if (user.role === 'STUDENT') window.location.href = 'student/dashboard.html';
        }
    } catch (err) {
        // Not logged in, stay on login page
        console.log('Not logged in');
    }
}

async function handleLogin(event) {
    event.preventDefault();
    
    if (!validateUsername() || !validatePassword()) {
        return;
    }
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    try {
        const user = await apiCall('/auth/login', 'POST', { username, password });
        
        // Store user info in session storage
        sessionStorage.setItem('userId', user.id);
        sessionStorage.setItem('userRole', user.role);
        sessionStorage.setItem('username', user.username);
        
        // Redirect based on role
        if (user.role === 'ADMIN') {
            window.location.href = 'admin/dashboard.html';
        } else if (user.role === 'TEACHER') {
            window.location.href = 'teacher/dashboard.html';
        } else if (user.role === 'STUDENT') {
            window.location.href = 'student/dashboard.html';
        } else {
            showError('Invalid user role');
        }
    } catch (err) {
        showError('Invalid username or password');
    }
}

function showError(message) {
    const errorDiv = document.getElementById('error');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
    
    // Hide after 3 seconds
    setTimeout(() => {
        errorDiv.style.display = 'none';
    }, 3000);
}