package com.udacity

data class Project(val name: String, val description: String, val url: String) {

    companion object {
        @JvmStatic
        fun getGlide(): Project {
            return Project(
                "Glide",
                "Image Loading Library by BumpTech",
                "https://github.com/bumptech/glide"
            )
        }

        @JvmStatic
        fun getLoadApp(): Project {
            return Project(
                "LoadApp",
                "Current repository by Udacity",
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
            )
        }

        @JvmStatic
        fun getRetrofit(): Project {
            return Project(
                "Retrofit",
                "Type-safe HTTP client for Android and Java by Square, Inc",
                "https://github.com/square/retrofit"
            )
        }
    }
}
