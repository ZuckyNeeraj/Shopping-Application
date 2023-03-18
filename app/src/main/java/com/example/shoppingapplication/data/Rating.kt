package com.example.shoppingapplication.data

import com.google.errorprone.annotations.Keep
import java.io.Serializable

@Keep
data class Rating(
    val count: Int = 0,
    val rate: Double = 0.0
): Serializable