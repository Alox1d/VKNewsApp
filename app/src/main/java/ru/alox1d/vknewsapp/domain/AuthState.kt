package ru.alox1d.vknewsapp.domain

sealed class AuthState {
    data object Authorized : AuthState()
    data object NotAuthorized : AuthState()
    data object Initial : AuthState()
}