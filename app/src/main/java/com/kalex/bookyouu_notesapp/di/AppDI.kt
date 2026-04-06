package com.kalex.bookyouu_notesapp.di

import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import androidx.room.Room
import com.kalex.bookyouu_notesapp.MainActivity
import com.kalex.bookyouu_notesapp.db.BookYouuDataBase
import com.kalex.bookyouu_notesapp.db.DBConstants
import com.kalex.bookyouu_notesapp.moreMenu.MoreMenuFlagsUseCase
import com.kalex.bookyouu_notesapp.moreMenu.SwitchMenuViewModel
import com.kalex.bookyouu_notesapp.records.AudioRecordViewModel
import com.kalex.bookyouu_notesapp.records.PagingRecordsViewModel
import com.kalex.bookyouu_notesapp.records.RecordsViewModel
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import com.kalex.bookyouu_notesapp.records.data.NotesRepositoryImpl
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormInformationViewModel
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormViewModel
import com.kalex.bookyouu_notesapp.subject.data.SubjectRepository
import com.kalex.bookyouu_notesapp.subject.data.SubjectRepositoryImpl
import com.kalex.bookyouu_notesapp.subject.subjectList.presentation.SubjectListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { Intent(androidContext(), MainActivity::class.java) }
    single { MoreMenuFlagsUseCase(androidContext()) }
    single { Dispatchers.IO }
    single {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            MediaRecorder()
        } else {
            MediaRecorder(androidContext())
        }
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BookYouuDataBase::class.java,
            DBConstants.DB_NAME
        ).build()
    }
    single { get<BookYouuDataBase>().noteDao }
    single { get<BookYouuDataBase>().subjectDao }
    single { get<BookYouuDataBase>().obligationDao }
}

val repositoryModule = module {
    singleOf(::NotesRepositoryImpl) { bind<NotesRepository>() }
    singleOf(::SubjectRepositoryImpl) { bind<SubjectRepository>() }
}

val viewModelModule = module {
    viewModelOf(::SubjectFormViewModel)
    viewModelOf(::SubjectListViewModel)
    viewModelOf(::RecordsViewModel)
    viewModelOf(::PagingRecordsViewModel)
    viewModelOf(::AudioRecordViewModel)
    viewModelOf(::SubjectFormInformationViewModel)
    viewModelOf(::SwitchMenuViewModel)
}
