package com.aqchen.filterfiesta.domain.use_case.filters

import android.graphics.Bitmap
import android.net.Uri
import com.aqchen.filterfiesta.domain.models.Filter
import com.aqchen.filterfiesta.util.Resource
import java.io.File

class GenerateImageUseCase(
    private val getFilterClassFromTypeUseCase: GetFilterClassFromTypeUseCase
) {
    operator fun invoke(source: Bitmap, dest: Bitmap, filters: List<Filter>): Resource<Unit> {
        filters.forEach {
            val filter = getFilterClassFromTypeUseCase(it.type)
            filter?.apply(source, dest, it.parameters)
        }
        return Resource.Success(Unit)
    }
}
