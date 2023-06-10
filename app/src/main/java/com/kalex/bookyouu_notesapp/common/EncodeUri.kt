package com.kalex.bookyouu_notesapp.common

import android.net.Uri

fun String.encodeUri() {
    Uri.encode(this.replace('%', '|'))
}