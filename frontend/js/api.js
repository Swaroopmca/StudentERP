const API_BASE = 'http://localhost:8080/api';

async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include' // Important for session cookies
    };
    
    if (body) {
        options.body = JSON.stringify(body);
    }
    
    try {
        const response = await fetch(API_BASE + endpoint, options);
        
        // Handle 401 Unauthorized - redirect to login
        if (response.status === 401) {
            const currentPage = window.location.pathname.split('/').pop();
            if (currentPage !== 'login.html') {
                sessionStorage.clear();
                window.location.href = '../login.html';
            }
            throw new Error('Session expired. Please login again.');
        }
        
        // Check if response is ok
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || response.statusText);
        }
        
        // Check if response has content
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        } else {
            return await response.text();
        }
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

// Helper function for file uploads
async function apiUpload(endpoint, formData) {
    const options = {
        method: 'POST',
        body: formData,
        credentials: 'include'
    };
    
    const response = await fetch(API_BASE + endpoint, options);
    if (!response.ok) {
        throw new Error(await response.text());
    }
    return response.json();
}

// Helper function for downloading files
async function apiDownload(endpoint) {
    const response = await fetch(API_BASE + endpoint, {
        credentials: 'include'
    });
    
    if (!response.ok) {
        throw new Error(await response.text());
    }
    
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = endpoint.split('/').pop() || 'download';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
}

// Helper function to check if user is logged in
async function checkAuth() {
    try {
        await apiCall('/auth/me');
        return true;
    } catch {
        return false;
    }
}

// Export for use in other files
window.apiCall = apiCall;
window.apiUpload = apiUpload;
window.apiDownload = apiDownload;
window.checkAuth = checkAuth;