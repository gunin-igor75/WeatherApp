package com.github.gunin_igor75.weatherapp.domain.useCase

import com.github.gunin_igor75.weatherapp.domain.repositoriy.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String) = repository.search(query)
}