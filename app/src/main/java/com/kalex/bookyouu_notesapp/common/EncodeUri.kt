package com.kalex.bookyouu_notesapp.common

import android.net.Uri

fun String.encodeUri(): String = Uri.encode(this.replace('%', '|'))
