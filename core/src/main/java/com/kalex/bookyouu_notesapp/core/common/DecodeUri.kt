package com.kalex.bookyouu_notesapp.core.common

import android.net.Uri

fun String.decodeUri(): Uri = this.replace('|', '%').let(Uri::parse)

