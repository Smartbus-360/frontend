//package com.smartbus360.app.parent.models
//
////data class DashboardResponse(
////    val studentName: String,
////    val className: String,
////    val section: String?,
////    val attendancePercentage: Int,
////    val homeworkCount: Int,
////    val upcomingExams: Int,
////    val feesDue: Int
////)
//data class DashboardResponse(
//    val studentName: String,
//    val className: String,
//    val attendancePercentage: Int,
//    val homeworkCount: Int,
//    val feesDue: Int
//)
//
//data class FeesDueResponse(
//    val installments: List<FeeInstallment>
//)
//data class FeesHistoryResponse(
//    val history: List<FeeInstallment>
//)
//data class PayFeeResponse(
//    val success: Boolean,
//    val message: String,
//    val receiptUrl: String?
//)
//data class ExamScheduleResponse(
//    val exams: List<ExamItem>
//)
//data class ExamResultsResponse(
//    val results: List<ExamSubject>
//)
//data class LeaveRequest(
//    val fromDate: String,
//    val toDate: String,
//    val reason: String
//)
//data class LeaveResponse(
//    val success: Boolean,
//    val message: String
//)
//data class LeaveHistoryResponse(
//    val leaves: List<LeaveItem>
//)
//data class ReplyRequest(
//    val message: String
//)
//data class ReplyResponse(
//    val success: Boolean,
//    val message: String
//)
//

package com.smartbus360.app.parent.models

data class DashboardResponse(
    val success: Boolean,
    val student: Student,
    val homework: List<HomeworkItem>,
    val timetable: List<Any>,
    val attendance: List<AttendanceItem>,
    val exam: ExamItem?,
    val marks: List<Any>
)

data class Student(
    val id: Int,
    val full_name: String,
    val username: String,
    val email: String,
    val phone: String?,
    val address: String?,
    val profilePicture: String?,
    val classId: Int,
    val sectionId: Int,
    val stopId: Int?
)

data class AttendanceItem(
    val id: Int,
    val studentId: Int,
    val date: String,
    val status: String // "P" or "A"
)

//data class ExamItem(
//    val id: Int,
//    val examName: String,
//    val date: String
//)
