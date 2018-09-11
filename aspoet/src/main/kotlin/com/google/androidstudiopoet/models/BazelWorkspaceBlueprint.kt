/*
 *  Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.joinPath

class BazelWorkspaceBlueprint(projectRoot: String) {

  val workspacePath = projectRoot.joinPath("WORKSPACE")

  val bazelWorkspaceContent = """android_sdk_repository(name = "androidsdk")
# Google Maven Repository
GMAVEN_TAG = "20180607-1"

http_archive(
    name = "gmaven_rules",
    strip_prefix = "gmaven_rules-%s" % GMAVEN_TAG,
    urls = ["https://github.com/bazelbuild/gmaven_rules/archive/%s.tar.gz" % GMAVEN_TAG],
)

load("@gmaven_rules//:gmaven.bzl", "gmaven_rules")
gmaven_rules()
"""

}

