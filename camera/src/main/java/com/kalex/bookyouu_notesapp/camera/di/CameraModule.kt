package com.kalex.bookyouu_notesapp.camera.di

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.kalex.bookyouu_notesapp.camera.data.engine.MlKitReceiptOcrEngine
import com.kalex.bookyouu_notesapp.camera.data.parser.RegexReceiptTextParser
import com.kalex.bookyouu_notesapp.camera.domain.engine.ReceiptOcrEngine
import com.kalex.bookyouu_notesapp.camera.domain.parser.ReceiptTextParser
import com.kalex.bookyouu_notesapp.camera.domain.usecase.ScanReceiptUseCase
import com.kalex.bookyouu_notesapp.camera.presentation.CameraScannerViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cameraModule = module {
    single<TextRecognizer> { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    singleOf(::MlKitReceiptOcrEngine) { bind<ReceiptOcrEngine>() }
    singleOf(::RegexReceiptTextParser) { bind<ReceiptTextParser>() }
    singleOf(::ScanReceiptUseCase)
    viewModelOf(::CameraScannerViewModel)
}
