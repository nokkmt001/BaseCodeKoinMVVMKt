package com.phat.testbase.dev.utils

object ContainsUtils {

    const val baseUrl = "https://www.google.com/"

    const val baseUrlT = "https://gasanphat.com/"

    var HEADER_AUTHORIZATION = "Authorization"
    var HEADER_LANG = "Accept-Language"
    var HEADER_API_KEY = "x-api-key"
    var HEADER_UUID = "x-request-id"
    var HEADER_HEAD_TOKEN = "BEARER "
    var HEADER_CONTENT_TYPE = "Content-Type"
    var HEADER_CONTENT_TYPE_VALUE_JSON = "application/json"

    var RESPONSE_MESSAGE_OK = "OK "
    var HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded"

    /**
     * Use for upload image --> part
     */
    var UPLOAD_IMAGE_PART = "file\"; filename=\"_img.png\" "

    /**
     * Use for upload image --> Content type
     */
    var HEADER_CONTENT_TYPE_IMAGE = "multipart/form-data"

    var TIME_OUT = 60L

    /**
     * multi language, =true if has 2 or more language
     */
    var isMultiLanguage = true

    var DEFAULT_LANGUAGE = "vi"

    var API_KEY_TOKEN = "BEARER "

}