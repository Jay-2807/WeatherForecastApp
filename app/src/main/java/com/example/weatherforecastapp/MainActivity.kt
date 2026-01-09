package com.example.weatherforecastapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherforecastapp.data.api.RetrofitObject
import com.example.weatherforecastapp.data.db.WeatherRoomDb
import com.example.weatherforecastapp.data.model.WeatherApiDbModel
import com.example.weatherforecastapp.data.repo.WeatherApiRepo
import com.example.weatherforecastapp.data.viewmodel.WeatherApiViewModel
import com.example.weatherforecastapp.data.viewmodel.WeatherViewModelFactory
import com.example.weatherforecastapp.ui.theme.WeatherForecastAppTheme
import com.example.weatherforecastapp.util.DateUtil


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = WeatherRoomDb.getDatabase(this)
        val repository = WeatherApiRepo(
            api = RetrofitObject.api,
            dao = db.weatherDao(),
            context = this
        )

        val factory = WeatherViewModelFactory(repository)
        enableEdgeToEdge()
        setContent {
            WeatherForecastAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(

                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel(factory = factory)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(modifier: Modifier,viewModel: WeatherApiViewModel = viewModel()) {


    var city by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.fetchWeather(city)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else if(viewModel.errorMessage !=null ){


            Text(
                text = viewModel.errorMessage!!,
                color = Color.Red,
                textAlign = TextAlign.Center
            )


        } else{
            LazyColumn {
                items(viewModel.weatherList) { item ->
                    WeatherItem(item)
                }
            }
        }
    }
}


@Composable
fun WeatherItem(item: WeatherApiDbModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = DateUtil.formatWeatherDate(item.date),
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(
                        id = weatherIconRes(item.condition)
                    ),
                    contentDescription = item.condition,
                    modifier = Modifier.size(22.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(item.temp.toString())
            }

            Text(
                text = item.condition,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}



@DrawableRes
fun weatherIconRes(condition: String): Int {
    val text = condition.lowercase()

    return when {
        text.contains("rain") ->
            R.drawable.baseline_cloudy_snowing_24

        text.contains("cloud") ->
            R.drawable.baseline_cloud_queue_24

        text.contains("sun") || text.contains("clear") ->
            R.drawable.baseline_sunny_24

        else ->
            R.drawable.baseline_sunny_24
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val viewModel: WeatherApiViewModel = viewModel()
    WeatherForecastAppTheme {
        Greeting(Modifier, viewModel = viewModel)
    }
}


