package com.example.kvest2.data.model

interface ApiResult {
    var message: String
    var isError: Boolean
}

class ApiFetchResult (
    override var message: String,
    override var isError: Boolean
) : ApiResult