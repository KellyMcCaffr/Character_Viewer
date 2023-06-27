package com.sample.mvm

import java.util.HashMap

data class ResponseItem internal constructor(
    var Icon: HashMap<String, String>?,
    var Result: String?,
    var Text: String?
)