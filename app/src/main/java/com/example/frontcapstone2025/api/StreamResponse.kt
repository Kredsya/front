package com.example.frontcapstone2025.api

data class StreamResponse(
    val targets: List<List<String>>
) {
    fun getSrc(): List<String> {
        return targets.mapNotNull { it.getOrNull(0) }
    }
}