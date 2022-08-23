package com.example.canvaspaint_lytnevaag

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel{
        CanvasViewModel()
    }
}