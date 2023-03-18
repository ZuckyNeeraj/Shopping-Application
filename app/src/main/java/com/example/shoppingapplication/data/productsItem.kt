package com.example.shoppingapplication.data

import com.google.errorprone.annotations.Keep
import java.io.Serializable


@Keep
data class productsItem(
    val description: String? = null,
    val id: Int? = null,
    val image: String? = null,
    val price: Double? = null,
    val rating: Rating? = null,
    val title: String? = null
): Serializable