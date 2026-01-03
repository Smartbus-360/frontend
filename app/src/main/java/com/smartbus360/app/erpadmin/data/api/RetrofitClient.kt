package com.smartbus360.app.erpadmin.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://erp.smartbus360.com/" // change if local

    // ✅ SINGLE RETROFIT INSTANCE
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ✅ ADMIN AUTH
    val api: AdminApiService by lazy {
        retrofit.create(AdminApiService::class.java)
    }

    // ✅ TEACHER
    val teacherApi: TeacherApiService by lazy {
        retrofit.create(TeacherApiService::class.java)
    }

    // ✅ STUDENT
    val studentApi: StudentApiService by lazy {
        retrofit.create(StudentApiService::class.java)
    }

    // ✅ CLASS & SECTION
    val classSectionApi: ClassSectionApiService by lazy {
        retrofit.create(ClassSectionApiService::class.java)
    }

    // ✅ EXAMS (ADMIN – READ ONLY)
    val examAdminApi: ExamAdminApiService by lazy {
        retrofit.create(ExamAdminApiService::class.java)
    }

    // ✅ SYLLABUS PROGRESS (ADMIN – READ ONLY)
    val syllabusAdminApi: SyllabusAdminApiService by lazy {
        retrofit.create(SyllabusAdminApiService::class.java)
    }
}
