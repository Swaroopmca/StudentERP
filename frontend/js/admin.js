// Admin module specific JavaScript
class AdminModule {
    constructor() {
        this.init();
    }
    
    async init() {
        await this.loadDashboardStats();
        this.setupEventListeners();
    }
    
    async loadDashboardStats() {
        try {
            const stats = await apiCall('/admin/dashboard/stats');
            this.displayStats(stats);
            await this.loadRecentActivity();
            await this.loadFeeReport();
        } catch (err) {
            console.error('Failed to load dashboard stats:', err);
        }
    }
    
    displayStats(stats) {
        document.getElementById('totalStudents').textContent = stats.totalStudents || 0;
        document.getElementById('totalTeachers').textContent = stats.totalTeachers || 0;
        document.getElementById('totalCourses').textContent = stats.totalCourses || 0;
        document.getElementById('totalSubjects').textContent = stats.totalSubjects || 0;
        document.getElementById('pendingFees').textContent = stats.pendingFees || 0;
    }
    
    async loadRecentActivity() {
        // Load recent notices
        try {
            const notices = await apiCall('/notices/latest?limit=5');
            this.displayRecentNotices(notices);
        } catch (err) {
            console.error('Failed to load notices:', err);
        }
    }
    
    displayRecentNotices(notices) {
        const container = document.getElementById('recentNotices');
        if (!container) return;
        
        let html = '<table><tr><th>Title</th><th>Posted Date</th></tr>';
        notices.forEach(notice => {
            html += `<tr>`;
            html += `<td>${notice.title}</td>`;
            html += `<td>${new Date(notice.postedAt).toLocaleDateString()}</td>`;
            html += `</tr>`;
        });
        html += '</table>';
        container.innerHTML = html;
    }
    
    async loadFeeReport() {
        try {
            const report = await apiCall('/admin/reports/fees-collection');
            document.getElementById('totalCollected').textContent = '₹' + (report.totalCollected || 0);
            document.getElementById('totalPending').textContent = '₹' + (report.totalPending || 0);
        } catch (err) {
            console.error('Failed to load fee report:', err);
        }
    }
    
    async loadStudentDistribution() {
        try {
            const distribution = await apiCall('/admin/reports/students-by-course');
            this.displayStudentDistribution(distribution);
        } catch (err) {
            console.error('Failed to load student distribution:', err);
        }
    }
    
    displayStudentDistribution(distribution) {
        const container = document.getElementById('studentDistribution');
        if (!container) return;
        
        let html = '<table><tr><th>Course</th><th>Students</th></tr>';
        for (const [course, count] of Object.entries(distribution)) {
            html += `<tr><td>${course}</td><td>${count}</td></tr>`;
        }
        html += '</table>';
        container.innerHTML = html;
    }
    
    async deleteUser(userId, userType) {
        if (!confirm(`Are you sure you want to delete this ${userType}?`)) {
            return;
        }
        
        try {
            await apiCall(`/admin/users/${userId}`, 'DELETE');
            alert(`${userType} deleted successfully`);
            location.reload();
        } catch (err) {
            console.error('Failed to delete user:', err);
            alert('Failed to delete user');
        }
    }
    
    setupEventListeners() {
        const logoutBtn = document.querySelector('a[href="../login.html"]');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.logout();
            });
        }
    }
    
    async logout() {
        try {
            await apiCall('/auth/logout', 'POST');
            sessionStorage.clear();
            window.location.href = '../login.html';
        } catch (err) {
            console.error('Logout failed:', err);
        }
    }
}

// Initialize module
document.addEventListener('DOMContentLoaded', () => {
    const adminModule = new AdminModule();
    
    // Load additional data based on page
    if (document.getElementById('studentDistribution')) {
        adminModule.loadStudentDistribution();
    }
});