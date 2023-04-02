package com.aqchen.filterfiesta.domain.use_case.filters

import android.graphics.Bitmap
import android.net.Uri
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GenerateImageUseCase(
    private val getFilterClassFromTypeUseCase: GetFilterClassFromTypeUseCase
) {
    suspend operator fun invoke(source: Bitmap, filters: List<Filter>): Bitmap? {
        var currentBitmap: Bitmap = source
        var success = false
        // apply filters in coroutine
        withContext(Dispatchers.Default) {
            filters.forEach {
                val filter = getFilterClassFromTypeUseCase(it.type) ?: return@withContext
                currentBitmap = filter.apply(currentBitmap, it.parameters) ?: return@withContext
            }
            success = true
        }
        if (!success) {
            return null
        }
        return currentBitmap
    }
}
