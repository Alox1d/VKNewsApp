package ru.alox1d.vknewsapp

sealed class AuthState {
    data object Authorized : AuthState()
    data object NotAuthorized : AuthState()
    data object Initial : AuthState()
}