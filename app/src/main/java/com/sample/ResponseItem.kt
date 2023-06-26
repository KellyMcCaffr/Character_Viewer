package com.sample

import java.util.HashMap

data class ResponseItem internal constructor(
    var FirstURL: String?,
    var Icon: HashMap<String, String>?,
    var Result: String?,
    var Text: String?
)