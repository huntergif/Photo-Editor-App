package com.aqchen.filterfiesta.domain.use_case.filters

import android.net.Uri
import com.aqchen.filterfiesta.util.Resource
import java.io.File
import java.nio.file.Files

class CopyImageUseCase {
    suspend operator fun invoke(src: Uri, dest: Uri): Resource<Unit> {
        val srcPath = src.path
        val destPath = dest.path
        if (srcPath == null || destPath == null) {
            return Resource.Error("Source or destination path is null")
        }
        val srcFile = File(srcPath)
        val destFile = File(destPath)
        srcFile.copyTo(destFile, overwrite = true)
        return Resource.Success(Unit)
    }
}
