package com.kalex.bookyouu_notesapp.common

import android.net.Uri

fun String.decodeUri(): Uri = this.replace('|', '%').let(Uri::parse)

