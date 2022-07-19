package com.phat.testbase.model

/**
 * @param state response code for status response when request from api
 * @param code result status code api
 */
class UiState<T>(var state: RepoState = RepoState.NON, var result: T? = null, var message: String? = null, var code: Int? = null)

enum class RepoState(val state: Int) {
    NON(-1), SUCCESS(1), FAIL(0);
}