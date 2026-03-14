// Teacher module specific JavaScript
class TeacherModule {
    constructor() {
        this.teacherId = sessionStorage.getItem('userId');
        this.init();
    }
    
    async init() {
        await this.loadTeacherProfile();
        await this.loadSubjects();
        this.setupEventListeners();
    }
    
    async loadTeacherProfile() {
        try {
            const teacher = await apiCall('/teachers/me');
            document.getElementById('teacherName').textContent = 
                teacher.firstName + ' ' + teacher.lastName;
        } catch (err) {
            console.error('Failed to load profile:', err);
            window.location.href = '../login.html';
        }
    }
    
    async loadSubjects() {
        try {
            const subjects = await apiCall('/subjects/teacher/me');
            this.populateSubjectDropdowns(subjects);
        } catch (err) {
            console.error('Failed to load subjects:', err);
        }
    }
    
    populateSubjectDropdowns(subjects) {
        const subjectSelects = document.querySelectorAll('.subject-select');
        subjectSelects.forEach(select => {
            select.innerHTML = '<option value="">Select Subject</option>';
            subjects.forEach(subject => {
                const option = document.createElement('option');
                option.value = subject.id;
                option.textContent = subject.name;
                select.appendChild(option);
            });
        });
    }
    
    async loadStudentsForAttendance() {
        const subjectId = document.getElementById('attendanceSubject').value;
        const date = document.getElementById('attendanceDate').value;
        
        if (!subjectId || !date) {
            alert('Please select subject and date');
            return;
        }
        
        try {
            const subject = await apiCall('/subjects/' + subjectId);
            const students = await apiCall('/students/course/' + subject.course.id);
            const existingAttendance = await apiCall(`/attendance/subject/${subjectId}/date/${date}`);
            
            this.displayAttendanceForm(students, existingAttendance);
        } catch (err) {
            console.error('Failed to load students:', err);
        }
    }
    
    displayAttendanceForm(students, existingAttendance) {
        const container = document.getElementById('attendanceContainer');
        const attendanceMap = {};
        existingAttendance.forEach(a => { attendanceMap[a.student.id] = a.status; });
        
        let html = '<table><tr><th>Student</th><th>Status</th></tr>';
        students.forEach(student => {
            html += `<tr>`;
            html += `<td>${student.firstName} ${student.lastName}</td>`;
            html += `<td>`;
            html += `<select class="attendance-status" data-student-id="${student.id}">`;
            html += `<option value="PRESENT" ${attendanceMap[student.id]==='PRESENT'?'selected':''}>Present</option>`;
            html += `<option value="ABSENT" ${attendanceMap[student.id]==='ABSENT'?'selected':''}>Absent</option>`;
            html += `<option value="LATE" ${attendanceMap[student.id]==='LATE'?'selected':''}>Late</option>`;
            html += `</select>`;
            html += `</td>`;
            html += `</tr>`;
        });
        html += '</table>';
        html += '<button onclick="teacherModule.saveAttendance()">Save Attendance</button>';
        
        container.innerHTML = html;
    }
    
    async saveAttendance() {
        const subjectId = document.getElementById('attendanceSubject').value;
        const date = document.getElementById('attendanceDate').value;
        const statusSelects = document.querySelectorAll('.attendance-status');
        
        const attendanceData = [];
        statusSelects.forEach(select => {
            attendanceData.push({
                student: { id: parseInt(select.dataset.studentId) },
                subject: { id: parseInt(subjectId) },
                date: date,
                status: select.value,
                markedBy: { id: parseInt(this.teacherId) }
            });
        });
        
        try {
            await apiCall('/attendance/bulk', 'POST', attendanceData);
            alert('Attendance saved successfully');
        } catch (err) {
            console.error('Failed to save attendance:', err);
            alert('Failed to save attendance');
        }
    }
    
    async loadStudentsForMarks() {
        const subjectId = document.getElementById('marksSubject').value;
        
        if (!subjectId) {
            alert('Please select subject');
            return;
        }
        
        try {
            const subject = await apiCall('/subjects/' + subjectId);
            const students = await apiCall('/students/course/' + subject.course.id);
            this.displayMarksForm(students, subjectId);
        } catch (err) {
            console.error('Failed to load students:', err);
        }
    }
    
    displayMarksForm(students, subjectId) {
        const container = document.getElementById('marksContainer');
        
        let html = '<table><tr><th>Student</th><th>Exam Type</th><th>Marks</th></tr>';
        students.forEach(student => {
            html += `<tr>`;
            html += `<td>${student.firstName} ${student.lastName}</td>`;
            html += `<td>`;
            html += `<select class="marks-examtype" data-student-id="${student.id}">`;
            html += `<option value="MIDTERM">Mid Term</option>`;
            html += `<option value="FINAL">Final</option>`;
            html += `<option value="ASSIGNMENT">Assignment</option>`;
            html += `</select>`;
            html += `</td>`;
            html += `<td><input type="number" class="marks-obtained" data-student-id="${student.id}" step="0.01"></td>`;
            html += `</tr>`;
        });
        html += '</table>';
        html += '<div>';
        html += '<label>Max Marks: <input type="number" id="maxMarks" value="100"></label>';
        html += '</div>';
        html += '<button onclick="teacherModule.saveMarks()">Save Marks</button>';
        
        container.innerHTML = html;
    }
    
    async saveMarks() {
        const subjectId = document.getElementById('marksSubject').value;
        const maxMarks = document.getElementById('maxMarks').value;
        const examDate = document.getElementById('examDate').value;
        
        const marksData = [];
        const examTypeSelects = document.querySelectorAll('.marks-examtype');
        const marksInputs = document.querySelectorAll('.marks-obtained');
        
        examTypeSelects.forEach((select, index) => {
            const studentId = select.dataset.studentId;
            const marksObtained = marksInputs[index].value;
            
            if (marksObtained) {
                marksData.push({
                    student: { id: parseInt(studentId) },
                    subject: { id: parseInt(subjectId) },
                    examType: select.value,
                    marksObtained: parseFloat(marksObtained),
                    maxMarks: parseFloat(maxMarks),
                    examDate: examDate,
                    enteredBy: { id: parseInt(this.teacherId) }
                });
            }
        });
        
        try {
            await apiCall('/marks/bulk', 'POST', marksData);
            alert('Marks saved successfully');
        } catch (err) {
            console.error('Failed to save marks:', err);
            alert('Failed to save marks');
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
let teacherModule;
document.addEventListener('DOMContentLoaded', () => {
    teacherModule = new TeacherModule();
});