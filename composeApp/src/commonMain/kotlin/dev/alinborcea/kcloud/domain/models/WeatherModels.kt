package dev.alinborcea.kcloud.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val location: Location = Location(),
    val current: Current = Current(),
    val forecast: Forecast = Forecast()
)


@Serializable
data class Forecast(
    @SerialName("forecastday") val forecastDay: List<ForecastDay> = emptyList()
)

@Serializable
data class ForecastDay(
    val date: String = "",
    val day: Day = Day(),
    val hour: List<Hour> = emptyList()
)

@Serializable
data class Hour(
    @SerialName("time_epoch") val timeEpoch: Long = 0L,
    val time: String = "", // Format: "2024-05-13 00:00"
    @SerialName("temp_c") val tempC: Double = 0.0,
    @SerialName("temp_f") val tempF: Double = 0.0,
    @SerialName("is_day") val isDay: Int = 0,
    val condition: Condition = Condition(),
)

@Serializable
data class Day(
    @SerialName("maxtemp_c") val maxTempC: Double = 0.0,
    @SerialName("mintemp_c") val minTempC: Double = 0.0,
    val condition: Condition = Condition()
) {
    fun maxTemp(useMetric: Boolean): Int =
        if (useMetric) maxTempC.toInt() else (maxTempC * 1.8 + 32).toInt()

    fun minTemp(useMetric: Boolean): Int =
        if (useMetric) minTempC.toInt() else (minTempC * 1.8 + 32).toInt()
}

@Serializable
data class Location(
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    @SerialName("tz_id") val tzId: String = "",
    @SerialName("localtime_epoch") val localtimeEpoch: Long = 0L,
    val localtime: String = ""
)

@Serializable
data class Current(
    @SerialName("last_updated_epoch") val lastUpdatedEpoch: Long = 0L,
    @SerialName("last_updated") val lastUpdated: String = "",
    @SerialName("temp_c") val tempC: Double = 0.0,
    @SerialName("temp_f") val tempF: Double = 0.0,
    @SerialName("is_day") val isDay: Int = 0,
    val condition: Condition = Condition(),
    @SerialName("wind_mph") val windMph: Double = 0.0,
    @SerialName("wind_kph") val windKph: Double = 0.0,
    @SerialName("wind_degree") val windDegree: Int = 0,
    @SerialName("wind_dir") val windDir: String = "",
    @SerialName("pressure_mb") val pressureMb: Double = 0.0,
    @SerialName("pressure_in") val pressureIn: Double = 0.0,
    @SerialName("precip_mm") val precipMm: Double = 0.0,
    @SerialName("precip_in") val precipIn: Double = 0.0,
    val humidity: Int = 0,
    val cloud: Int = 0,
    @SerialName("feelslike_c") val feelslikeC: Double = 0.0,
    @SerialName("feelslike_f") val feelslikeF: Double = 0.0,
    @SerialName("windchill_c") val windchillC: Double = 0.0,
    @SerialName("windchill_f") val windchillF: Double = 0.0,
    @SerialName("heatindex_c") val heatindexC: Double = 0.0,
    @SerialName("heatindex_f") val heatindexF: Double = 0.0,
    @SerialName("dewpoint_c") val dewpointC: Double = 0.0,
    @SerialName("dewpoint_f") val dewpointF: Double = 0.0,
    @SerialName("vis_km") val visKm: Double = 0.0,
    @SerialName("vis_miles") val visMiles: Double = 0.0,
    val uv: Double = 0.0,
    @SerialName("gust_mph") val gustMph: Double = 0.0,
    @SerialName("gust_kph") val gustKph: Double = 0.0,
    @SerialName("will_it_rain") val willItRain: Int = 0,
    @SerialName("chance_of_rain") val chanceOfRain: Int = 0,
    @SerialName("will_it_snow") val willItSnow: Int = 0,
    @SerialName("chance_of_snow") val chanceOfSnow: Int = 0,
    @SerialName("short_rad") val shortRad: Double = 0.0,
    @SerialName("diff_rad") val diffRad: Double = 0.0,
    val dni: Double = 0.0,
    val gti: Double = 0.0
) {
    fun temperature(metric: Boolean): Int = if (metric) tempC.toInt() else tempF.toInt()

    fun feelsLike(metric: Boolean): Int = if (metric) feelslikeC.toInt() else feelslikeF.toInt()

    fun wind(metric: Boolean): Int = if (metric) windKph.toInt() else windMph.toInt()

    fun pressure(metric: Boolean): Int = if (metric) pressureMb.toInt() else pressureIn.toInt()

    fun visibility(metric: Boolean): Int = if (metric) visKm.toInt() else visMiles.toInt()

    fun gust(metric: Boolean): Int = if (metric) gustKph.toInt() else gustMph.toInt()

}

@Serializable
data class Condition(
    val text: String = "",
    val icon: String = "",
    val code: Int = 0
)