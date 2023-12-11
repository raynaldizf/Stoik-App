package com.app.stoikapp.data.model

// Materi data class
data class Materi(
    val gambar: String? = null,
    val judul: String? = null,
    val penjelasan: String? = null,
    var isExpanded: Boolean = false // Add this property
)
