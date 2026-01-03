//package com.smartbus360.app.parent.api
//
//import com.smartbus360.app.parent.models.*
//import retrofit2.Response
//import retrofit2.http.*
//
//interface ParentApiService {
//
//    @GET("parent/dashboard/{studentId}")
//    suspend fun getDashboard(
//        @Path("studentId") studentId: Int
//    ): Response<DashboardResponse>
//
//    @GET("parent/attendance/monthly/{studentId}")
//    suspend fun getMonthlyAttendance(
//        @Path("studentId") studentId: Int,
//        @Query("month") month: Int,
//        @Query("year") year: Int
//    ): Response<MonthlyAttendanceResponse>
//
//
//
//    @GET("parent/attendance/daily/{studentId}")
//    suspend fun getDailyAttendance(
//        @Path("studentId") studentId: Int
//    ): Response<DailyAttendanceResponse>
//
//    @GET("parent/attendance/summary/{studentId}")
//    suspend fun getAttendanceSummary(
//        @Path("studentId") studentId: Int
//    ): Response<AttendanceSummaryResponse>
//
//    @GET("parent/homework/{studentId}")
//    suspend fun getHomework(
//        @Path("studentId") studentId: Int
//    ): Response<HomeworkListResponse>
//
//    @GET("parent/homework/details/{id}")
//    suspend fun getHomeworkDetails(
//        @Path("id") id: Int
//    ): Response<HomeworkDetailsResponse>
//
////    @GET("parent/fees/due/{studentId}")
////    suspend fun getFeesDue(
////        @Path("studentId") studentId: Int
////    ): Response<FeesDueResponse>
////
////    @GET("parent/fees/history/{studentId}")
////    suspend fun getFeesHistory(
////        @Path("studentId") studentId: Int
////    ): Response<FeesHistoryResponse>
//
//    @POST("parent/fees/pay/{studentId}")
//    suspend fun payFees(
//        @Path("studentId") studentId: Int
//    ): Response<PayFeeResponse>
//
////    @GET("parent/timetable/{studentId}")
////    suspend fun getTimetable(
////        @Path("studentId") studentId: Int
////    ): Response<TimetableResponse>
//
//    @GET("parent/exams/{studentId}")
//    suspend fun getExamSchedule(
//        @Path("studentId") studentId: Int
//    ): Response<ExamScheduleResponse>
//
////    @GET("parent/exams/results/{studentId}")
////    suspend fun getExamResults(
////        @Path("studentId") studentId: Int
////    ): Response<ExamResultsResponse>
//
//    @POST("parent/leave/apply/{studentId}")
//    suspend fun applyLeave(
//        @Path("studentId") studentId: Int,
//        @Body body: ApplyLeaveRequest
//    ): Response<LeaveResponse>
//
//    @GET("parent/circulars/details/{id}")
//    suspend fun getCircularDetails(
//        @Path("id") id: Int
//    ): Response<CircularDetailsResponse>
//
//    @GET("parent/leave/history/{studentId}")
//    suspend fun getLeaveHistory(
//        @Path("studentId") studentId: Int
//    ): Response<LeaveHistoryResponse>
//
//    @GET("parent/messages/{studentId}")
//    suspend fun getMessages(
//        @Path("studentId") studentId: Int
//    ): Response<MessageListResponse>
//
//    @POST("parent/messages/reply/{studentId}")
//    suspend fun sendReply(
//        @Path("studentId") studentId: Int,
//        @Body body: ReplyRequest
//    ): Response<ReplyResponse>
//
//    @GET("parent/circulars/{studentId}")
//    suspend fun getCirculars(@Path("studentId") id: Int): Response<CircularListResponse>
//
//    @POST("login/user")
//    suspend fun parentLogin(
//        @Body body: LoginRequest
//    ): Response<LoginResponse>
//
//    @GET("parent/profile")
//    suspend fun getParentProfile(): Response<ParentProfileResponse>
//
//    @GET("parent/exams/{studentId}")
//    suspend fun getExamList(@Path("studentId") id: Int): Response<ExamListResponse>
//
//    @GET("parent/exams/results/{studentId}")
//    suspend fun getExamResults(@Path("studentId") id: Int): Response<ExamResultsResponse>
//
//    @GET("parent/fees/due/{studentId}")
//    suspend fun getFeesDue(@Path("studentId") id: Int): Response<FeesDueResponse>
//
//    @GET("parent/fees/history/{studentId}")
//    suspend fun getFeesHistory(@Path("studentId") id: Int): Response<FeesHistoryResponse>
//
//    @GET("parent/timetable/{studentId}")
//    suspend fun getTimetable(@Path("studentId") id: Int): Response<TimetableResponse>
//
//
//    @GET("/api/parent/leave/{studentId}")
//    suspend fun getLeaveList(@Path("studentId") studentId: Int): Response<LeaveListResponse>
//
//    @GET("/api/parent/leave/details/{id}")
//    suspend fun getLeaveDetails(@Path("id") id: Int): Response<LeaveDetailsResponse>
//
//    @GET("/parent/fees/summary/{studentId}")
//    suspend fun getFeesSummary(@Path("studentId") studentId: Int): Response<FeesSummaryResponse>
//
//    @GET("/parent/fees/list/{studentId}")
//    suspend fun getFeesList(@Path("studentId") studentId: Int): Response<FeesListResponse>
//
//    @GET("/parent/fees/details/{installmentId}")
//    suspend fun getFeesDetails(@Path("installmentId") installmentId: Int): Response<FeeDetailsResponse>
//
//    @GET("/parent/messages/details/{messageId}")
//    suspend fun getMessageDetails(@Path("messageId") messageId: Int): Response<MessageDetailsResponse>
//
//
////    @GET("/api/parent/exams/{studentId}")
////    suspend fun getExamList(@Path("studentId") studentId: Int): Response<ExamListResponse>
//
//    @GET("/api/parent/exams/details/{examId}")
//    suspend fun getExamDetails(@Path("examId") examId: Int): Response<ExamDetailsResponse>
//
//}


package com.smartbus360.app.parent.api

import com.smartbus360.app.parent.models.*
import retrofit2.Response
import retrofit2.http.*

data class LeaveApplyResponse(
    val success: Boolean,
    val message: String,
    val leave: LeaveItem
)

data class ReplyMessageRequest(
    val receiverId: Int,
    val message: String
)

data class GenericResponse(
    val success: Boolean,
    val message: String
)

interface ParentApiService {

    // ---------------- LOGIN ----------------
    @POST("login/user")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>


    // ---------------- DASHBOARD ----------------
    @GET("parent/dashboard/{studentId}")
    suspend fun dashboard(
        @Path("studentId") studentId: Int
    ): Response<DashboardResponse>


    // ---------------- ATTENDANCE ----------------
    @GET("parent/attendance/monthly/{studentId}")
    suspend fun monthlyAttendance(
        @Path("studentId") studentId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<MonthlyAttendanceResponse>

    @GET("parent/attendance/daily/{studentId}")
    suspend fun dailyAttendance(
        @Path("studentId") studentId: Int,
        @Query("date") date: String
    ): Response<DailyAttendanceResponse>


    // ---------------- HOMEWORK ----------------
    @GET("parent/homework/{studentId}")
    suspend fun homeworkList(
        @Path("studentId") studentId: Int
    ): Response<HomeworkListResponse>

    @GET("parent/homework/details/{id}")
    suspend fun homeworkDetails(
        @Path("id") homeworkId: Int
    ): Response<HomeworkDetailsResponse>


    // ---------------- EXAMS ----------------
    @GET("parent/exams/{studentId}")
    suspend fun examSchedule(
        @Path("studentId") studentId: Int
    ): Response<ExamScheduleResponse>

    @GET("parent/exams/results/{studentId}")
    suspend fun examResults(
        @Path("studentId") studentId: Int
    ): Response<ExamResultsResponse>


    // ---------------- TIMETABLE ----------------
    @GET("api/parent/timetable/{studentId}")
    suspend fun getTimetable(
        @Path("studentId") studentId: Int
    ): Response<TimetableResponse>


    // ---------------- FEES ----------------
    @GET("parent/fees/due/{studentId}")
    suspend fun feesDue(
        @Path("studentId") studentId: Int
    ): Response<FeesDueResponse>

    @GET("parent/fees/history/{studentId}")
    suspend fun feesHistory(
        @Path("studentId") studentId: Int
    ): Response<FeesHistoryResponse>


    // ---------------- LEAVE ----------------
    @POST("parent/leave/apply/{studentId}")
    suspend fun applyLeave(
        @Path("studentId") studentId: Int,
        @Body body: ApplyLeaveRequest
    ): Response<LeaveApplyResponse>

    @GET("parent/leave/history/{studentId}")
    suspend fun leaveHistory(
        @Path("studentId") studentId: Int
    ): Response<LeaveHistoryResponse>


    // ---------------- MESSAGES ----------------
    @GET("api/parent/messages/{studentId}")
    suspend fun getMessages(
        @Path("studentId") studentId: Int
    ): Response<MessageListResponse>

    @POST("api/parent/messages/reply/{studentId}")
    suspend fun replyMessage(
        @Path("studentId") studentId: Int,
        @Body body: ReplyMessageRequest
    ): Response<GenericResponse>

}
