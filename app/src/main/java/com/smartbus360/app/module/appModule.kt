package com.smartbus360.app.module

import androidx.room.Room
import com.smartbus360.app.data.database.AlertStatusRepository
import com.smartbus360.app.data.database.AppDatabase
import com.smartbus360.app.data.olaMaps.RouteViewModel
import com.smartbus360.app.data.repository.PreferencesRepository
import com.smartbus360.app.utility.NetworkMonitor
import com.smartbus360.app.viewModels.AdvertisementBannerViewModel
import com.smartbus360.app.viewModels.AlertStatusViewModel
import com.smartbus360.app.viewModels.BusLocationScreenViewModel
import com.smartbus360.app.viewModels.LanguageViewModel
import com.smartbus360.app.viewModels.LoginViewModel
import com.smartbus360.app.viewModels.MainScreenViewModel
import com.smartbus360.app.viewModels.NetworkViewModel
import com.smartbus360.app.viewModels.NotificationViewModel
import com.smartbus360.app.viewModels.OnboardingViewModel
import com.smartbus360.app.viewModels.ReachDateTimeViewModel
import com.smartbus360.app.viewModels.RoleSelectionViewModel
import com.smartbus360.app.viewModels.SnappingViewModel
import com.smartbus360.app.viewModels.SplashViewModel
import com.smartbus360.app.viewModels.StudentScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.smartbus360.app.teacher.viewModel.TeacherDashboardViewModel
import com.smartbus360.app.teacher.viewModel.AttendanceViewModel
import com.smartbus360.app.teacher.viewModel.HomeworkViewModel
import com.smartbus360.app.teacher.viewModel.ExamViewModel
import com.smartbus360.app.teacher.viewModel.SyllabusViewModel
import com.smartbus360.app.teacher.viewModel.TeacherAuthViewModel
import com.smartbus360.app.teacher.data.repository.TeacherRepository
import android.app.Application
import com.smartbus360.app.parent.auth.ParentLoginViewModel
import com.smartbus360.app.parent.dashboard.ParentDashboardViewModel
import com.smartbus360.app.parent.attendance.ParentAttendanceViewModel
import com.smartbus360.app.parent.homework.ParentHomeworkViewModel
import com.smartbus360.app.parent.exams.ParentExamViewModel
import com.smartbus360.app.parent.fees.ParentFeesViewModel
import com.smartbus360.app.parent.leave.ParentLeaveViewModel
import com.smartbus360.app.parent.messages.ParentMessageViewModel
import com.smartbus360.app.parent.timetable.ParentTimetableViewModel
import com.smartbus360.app.parent.auth.ParentAuthRepository

val appModule = module {

    single { PreferencesRepository(androidContext()) }
//    single {  NetworkMonitor(androidContext()) }
    viewModel { SplashViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { LanguageViewModel(get()) }
    viewModel { RoleSelectionViewModel(get()) }
    viewModel { LoginViewModel(androidContext(), get()) }
    viewModel { MainScreenViewModel(get()) }
    viewModel { BusLocationScreenViewModel(get()) }
    viewModel { StudentScreenViewModel(get()) }
    viewModel { SnappingViewModel() }
    viewModel { RouteViewModel() }
    viewModel { AlertStatusViewModel(get())}
    viewModel { AdvertisementBannerViewModel(get()) }
    single { NetworkMonitor(get()) }

    single {
        TeacherRepository()
    }


    viewModel { NetworkViewModel(get()) }
    viewModel {NotificationViewModel(get())}
    viewModel { ReachDateTimeViewModel(get()) }
    viewModel { TeacherAuthViewModel() }
    viewModel { TeacherDashboardViewModel(androidContext().applicationContext as Application) }
    viewModel { AttendanceViewModel() }
    viewModel { HomeworkViewModel() }
    viewModel { ExamViewModel() }
    viewModel { SyllabusViewModel() }
// ---------------- PARENT MODULE ----------------
    single {
        ParentAuthRepository(get())
    }

    viewModel { ParentLoginViewModel(get()) }
    viewModel { ParentDashboardViewModel() }
    viewModel { ParentAttendanceViewModel() }
    viewModel { ParentHomeworkViewModel() }
    viewModel { ParentExamViewModel() }
    viewModel { ParentFeesViewModel() }
    viewModel { ParentLeaveViewModel() }
    viewModel { ParentMessageViewModel() }
    viewModel { ParentTimetableViewModel() }


    // Teacher Module injections

}


val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
    single { get<AppDatabase>().alertStatusDao() }
}

val repositoryModule = module {
    single { AlertStatusRepository(get()) }
//    single { TeacherRepository(get()) }


}