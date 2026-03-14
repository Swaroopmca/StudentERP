// Student module specific JavaScript
class StudentModule {
    constructor() {
        this.studentId = sessionStorage.getItem('userId');
        this.init();
    }
    
    async init() {
        await this.loadStudentProfile();
        this.setupEventListeners();
    }
    
    async loadStudentProfile() {
        try {
            const student = await apiCall('/students/me');
            document.getElementById('studentName').textContent = 
                student.firstName + ' ' + student.lastName;
            document.getElementById('studentId').textContent = student.id;
            document.getElementById('studentEmail').textContent = student.email;
            document.getElementById('studentCourse').textContent = 
                student.course ? student.course.name : 'Not assigned';
        } catch (err) {
            console.error('Failed to load profile:', err);
            window.location.href = '../login.html';
        }
    }
    
    async loadAttendance() {
        try {
            const attendance = await apiCall('/attendance/student/me');
            this.displayAttendance(attendance);
        } catch (err) {
            console.error('Failed to load attendance:', err);
        }
    }
    
    displayAttendance(attendance) {
        const tbody = document.querySelector('#attendanceTable tbody');
        if (!tbody) return;
        
        tbody.innerHTML = '';
        attendance.forEach(record => {
            const row = tbody.insertRow();
            row.insertCell().textContent = record.date;
            row.insertCell().textContent = record.subject.name;
            row.insertCell().innerHTML = 
                `<span class="status-badge status-${record.status.toLowerCase()}">${record.status}</span>`;
        });
    }
    
    async loadResults() {
        try {
            const marks = await apiCall('/marks/student/me');
            this.displayResults(marks);
        } catch (err) {
            console.error('Failed to load results:', err);
        }
    }
    
    displayResults(marks) {
        const tbody = document.querySelector('#resultsTable tbody');
        if (!tbody) return;
        
        tbody.innerHTML = '';
        marks.forEach(mark => {
            const row = tbody.insertRow();
            row.insertCell().textContent = mark.subject.name;
            row.insertCell().textContent = mark.examType;
            row.insertCell().textContent = mark.marksObtained;
            row.insertCell().textContent = mark.maxMarks;
            row.insertCell().textContent = ((mark.marksObtained / mark.maxMarks) * 100).toFixed(2) + '%';
        });
    }
    
    async loadFees() {
        try {
            const fees = await apiCall('/fees/student/me');
            this.displayFees(fees);
        } catch (err) {
            console.error('Failed to load fees:', err);
        }
    }
    
    displayFees(fees) {
        const tbody = document.querySelector('#feesTable tbody');
        if (!tbody) return;
        
        tbody.innerHTML = '';
        let totalDue = 0;
        
        fees.forEach(fee => {
            const row = tbody.insertRow();
            row.insertCell().textContent = fee.feeType;
            row.insertCell().textContent = '₹' + fee.amount;
            row.insertCell().textContent = fee.dueDate;
            row.insertCell().textContent = fee.paidDate || '-';
            row.insertCell().innerHTML = 
                `<span class="status-badge status-${fee.status.toLowerCase()}">${fee.status}</span>`;
            
            if (fee.status !== 'PAID') {
                totalDue += fee.amount;
            }
        });
        
        document.getElementById('totalDue').textContent = '₹' + totalDue;
    }
    
    setupEventListeners() {
        // Logout button
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

// Initialize module based on page
document.addEventListener('DOMContentLoaded', () => {
    const studentModule = new StudentModule();
    
    // Load specific data based on current page
    if (document.getElementById('attendanceTable')) {
        studentModule.loadAttendance();
    }
    if (document.getElementById('resultsTable')) {
        studentModule.loadResults();
    }
    if (document.getElementById('feesTable')) {
        studentModule.loadFees();
    }
});