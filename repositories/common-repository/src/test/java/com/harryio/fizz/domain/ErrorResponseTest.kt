package com.harryio.fizz.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorResponseTest {

    @Test
    fun testErrorMsgIds() {
        assertEquals(R.string.error_duplicated_entry, ErrorResponse(8).errorMsgResId())
        assertEquals(R.string.error_service_offline, ErrorResponse(9).errorMsgResId())
        assertEquals(R.string.error_authentication_failed, ErrorResponse(14).errorMsgResId())
        assertEquals(R.string.error_invalid_login_credentials, ErrorResponse(26).errorMsgResId())
        assertEquals(R.string.error_invalid_login_credentials, ErrorResponse(30).errorMsgResId())
        assertEquals(R.string.error_account_disabled, ErrorResponse(31).errorMsgResId())
        assertEquals(R.string.error_account_not_verified, ErrorResponse(32).errorMsgResId())
        assertEquals(R.string.error_invalid_request_token, ErrorResponse(33).errorMsgResId())
        assertEquals(R.string.error_resource_unavailable, ErrorResponse(34).errorMsgResId())
        assertEquals(null, ErrorResponse(12).errorMsgResId())
    }
}