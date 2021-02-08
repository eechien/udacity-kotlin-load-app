package com.udacity

data class Project(val name: String, val description: String, val url: String) {

    companion object {
        private val _glide = Project(
            "Glide",
            "Image Loading Library by BumpTech",
            "https://github.com/bumptech/glide"
        )
        @JvmStatic
        fun getGlide(): Project {
            return _glide
        }

        private val _loadApp = Project(
            "LoadApp",
            "Current repository by Udacity",
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        )
        @JvmStatic
        fun getLoadApp(): Project {
            return _loadApp
        }

        private val _retrofit = Project(
            "Retrofit",
            "Type-safe HTTP client for Android and Java by Square, Inc",
            "https://github.com/square/retrofit"
        )
        @JvmStatic
        fun getRetrofit(): Project {
            return _retrofit
        }
    }
}
