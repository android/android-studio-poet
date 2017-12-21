/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.writers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*

class GithubDownloader {

    private data class GithubItem(
            val name: String? = null,
            val path: String? = null,
            val sha: String? = null,
            val size: Long = 0,
            val url: String? = null,
            val html_url: String? = null,
            val git_url: String? = null,
            val download_url: String? = null,
            val type: String? = null)

    fun downloadDir(githubPath: String, to: String): Boolean {

        FileUtils.deleteDirectory(File(to))
        File(to).mkdirs()

        val list = listUrl(githubPath)
        val result = ArrayList<Boolean>()
        list?.forEach { item ->
            if ("dir" == item.type) {
                val newDir = File(to, item.name)
                result.add(downloadDir(item.url!!, newDir.absolutePath))
            } else {
                val file = File(to, item.name)
                file.createNewFile()
                result.add(downloadFile(item.download_url!!, file))
            }
        }
        return !result.contains(java.lang.Boolean.FALSE)
    }

    private fun listUrl(url: String): List<GithubItem>? {
        val json = getStringFromUrl(url)
        val listType = object : TypeToken<List<GithubItem>>() {}.type
        return Gson().fromJson<List<GithubItem>>(json, listType)
    }

    private fun getStringFromUrl(url: String): String? {
        var out: String? = null
        try {
            Scanner(URL(url).openStream(), "UTF-8").
                    use({ scanner -> out = scanner.useDelimiter("\\A").next() })
        } catch (ex: IOException) {
        }

        return out
    }

    private fun downloadFile(url: String, file: File): Boolean {
        var success = true
        try {
            FileUtils.copyURLToFile(URL(url), file)
        } catch (ioex: IOException) {
            success = false
        }

        return success
    }
}