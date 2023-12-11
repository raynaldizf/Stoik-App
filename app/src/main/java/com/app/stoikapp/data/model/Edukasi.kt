package com.app.stoikapp.data.model

data class Edukasi(
    val deskripsi: String? = null,
    val judul: String? = null,
    val path : String? = null,
    val materi: Map<String, Materi>? = null,
    val submateri: Map<String, SubMateri>? = null
)