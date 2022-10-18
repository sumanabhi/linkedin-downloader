package com.hadiyarajesh.linkedin_downloader.ui.home

import androidx.lifecycle.ViewModel
import com.hadiyarajesh.linkedin_downloader.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

}
