package ru.alox1d.vknewsapp.domain

private const val TOKEN_VISIBLE_CHARACTERS = 10

internal fun formatToken(token: String): String = token.hideLastCharacters(TOKEN_VISIBLE_CHARACTERS)

private fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
    return if (this.length <= firstCharactersToKeepVisible) {
        this
    } else {
        this.substring(
            0,
            firstCharactersToKeepVisible
        ) + "..."
    }
}
