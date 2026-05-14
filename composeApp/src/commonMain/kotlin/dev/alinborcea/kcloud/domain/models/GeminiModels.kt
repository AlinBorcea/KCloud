package dev.alinborcea.kcloud.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Part(val text: String = "")

@Serializable
data class Content(val parts: List<Part> = emptyList())

@Serializable
data class Candidate(val content: Content)

@Serializable
data class Error(val message: String = "")

@Serializable
data class GenerateContentResponse(val error: Error? = null, val candidates: List<Candidate>? = null)

@kotlinx.serialization.Serializable
data class GenerateContentRequest(val contents: Content)