package com.example.canvaspaint_lytnevaag.di

import com.example.canvaspaint_lytnevaag.ui.CanvasViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel{
        CanvasViewModel()
    }
}