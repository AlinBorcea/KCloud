# KCloud 🌦️

KCloud is a sleek, modern weather and climate application built using **Kotlin Multiplatform (KMP)**. It provides users with real-time weather tracking, detailed atmospheric metrics, multi-day forecasts, and an AI-powered climate assistant that gives localized geographical climate insights.

## 📱 Features

* **Real-time Weather Data:** Up-to-the-minute tracking of temperature, weather conditions, and localized timestamps.
* **Detailed Meteorological Metrics:** Expanded views for specialized data points including wind speed, humidity, barometric pressure, UV index, visibility, cloud cover, precipitation chance, and wind gusts.
* **Multi-Day Forecast:** Quick-glance weather cards showing high/low temperature predictions for upcoming days.
* **AI Climate Assistant ("Ask AI"):** An integrated conversational feature providing deep-dive geographic and seasonal climate breakdowns.
* **User Preferences:** Customizable favorite locations and toggle support between Metric (`°C`, `km/h`) and Imperial (`°F`, `mph`) units.

---

## 🛠️ Tech Stack

* **Core Logic:** [Kotlin Multiplatform (KMP)](https://kotlinlang.org/docs/multiplatform.html) for shared business logic across platforms.
* **Weather Data API:** Powered by [WeatherAPI](https://www.weatherapi.com/) to fetch live atmospheric conditions and forecasts.
* **AI Assistant:** Powered by the [Gemini API](https://ai.google.dev/) to generate context-aware climate summaries based on user location preferences.

## 🚀 Getting Started

### Prerequisites
Before running or building the project, ensure you have obtained the necessary API keys:
1. Get a free API key from [WeatherAPI](https://www.weatherapi.com/).
2. Get an API key from Google AI Studio for the [Gemini API](https://ai.google.dev/).

### Configuration
Add your API keys to your project's local environment configuration (e.g., `local.properties` or build configuration files depending on your KMP setup):

```properties
WEATHER_API_KEY="your_weatherapi_key_here"
GEMINI_API_KEY="your_gemini_api_key_here"