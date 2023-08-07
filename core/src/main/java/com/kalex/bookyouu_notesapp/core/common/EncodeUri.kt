package com.kalex.bookyouu_notesapp.core.common

import android.net.Uri

fun String.encodeUri(): String = Uri.encode(this.replace('%', '|'))
