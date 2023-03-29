package com.aqchen.filterfiesta.domain.models.image

import android.net.Uri
import java.lang.Exception

data class Image(
    val imageUri: Uri,
    val width: Int,
    val height: Int,
)
