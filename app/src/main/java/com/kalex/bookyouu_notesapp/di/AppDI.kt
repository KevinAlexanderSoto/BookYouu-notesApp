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
import com.kalex.bookyouu_notesapp.journal.di.journalModule
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
    single { get<BookYouuDataBase>().journalEntryDao }
    single { get<BookYouuDataBase>().journalDao }
    single { get<BookYouuDataBase>().obligationDao }
}

val repositoryModule = module {
    // Repositories are now in journalModule
}

val viewModelModule = module {
    viewModelOf(::SwitchMenuViewModel)
}

