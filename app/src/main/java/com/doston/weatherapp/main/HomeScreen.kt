package com.doston.weatherapp.main


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.doston.weatherapp.api.NetworkResponse
import com.doston.weatherapp.data.Weather
import com.doston.weatherapp.viewModel.WeatherViewModel

@Composable
fun HomeScreen(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val savedCities = remember { mutableStateOf(emptyList<String>()) }

    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val lastCities = viewModel.getSavedCityList()
        savedCities.value = lastCities
        if (lastCities.isNotEmpty()) {
            city = lastCities[0]
            viewModel.getData(city)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = {
                            city = it
                            expanded = true
                        },
                        label = { Text("Search for any location") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    if (city.isNotBlank()) {
                        viewModel.getData(city)
                        keyboardController?.hide()
                        expanded = false
                        viewModel.saveCity(city)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }

            }
            if (expanded)
                savedCities.value.forEach {
                    CityItem(it, onClick = {
                        city = it
                        expanded = false
                        if (city.isNotBlank()) {
                            viewModel.getData(city)
                            keyboardController?.hide()
                            expanded = false
                            viewModel.saveCity(city)
                        }
                    })
                }

        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message, color = Color.Red)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }

            null -> {}
        }
    }
}

@Composable
fun CityItem(city: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 5.dp)
            .clickable { onClick() }) {
        Text(text = city, fontSize = 18.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun WeatherDetails(data: Weather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(28.dp),
                tint = Color(0xFF1E88E5)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${data.location.name},",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = data.location.country,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "${data.current.temp_c} °c",
            fontSize = 56.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(5.dp))

        AsyncImage(
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    WeatherKeyVal("Humidity", "${data.current.humidity}%")
                    Spacer(modifier = Modifier.height(2.dp))
                    WeatherKeyVal("UV Index", "${data.current.uv}")
                    Spacer(modifier = Modifier.height(2.dp))
                    WeatherKeyVal("Time", data.location.localtime.split(" ")[1])

                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    WeatherKeyVal("Wind", "${data.current.wind_kph} km/h")
                    Spacer(modifier = Modifier.height(2.dp))
                    WeatherKeyVal("Precipitation", "${data.current.precip_mm} mm")
                    Spacer(modifier = Modifier.height(2.dp))
                    WeatherKeyVal("Date", data.location.localtime.split(" ")[0])
                }

            }
        }
    }
}

@Composable
fun WeatherKeyVal(key: String, value: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = key, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Medium)

    }
}
