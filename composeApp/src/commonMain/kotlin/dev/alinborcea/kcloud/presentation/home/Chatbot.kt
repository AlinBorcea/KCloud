package dev.alinborcea.kcloud.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.alinborcea.kcloud.data.repositories.GeminiApi
import kotlinx.coroutines.runBlocking

@Composable
fun Chatbot(location: String) {
    val gemini = GeminiApi()
    var content by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).padding(8.dp)) {
        TextButton(onClick = {
            runBlocking {
                try {
                    content = generateContent(gemini, "What is the climate in $location")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }) { Text("Ask about the climate") }
        Text(content, modifier = Modifier.fillMaxWidth())
    }
}

suspend fun generateContent(api: GeminiApi, prompt: String): String {
    println("prompt = $prompt")
    val result = api.generateContent(prompt)
    return if (result.candidates != null) {
        result.candidates[0].content.parts[0].text
    } else {
        "No results"
    }
}