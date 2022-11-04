package com.weather.app.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.weather.app.R
import com.weather.app.domain.model.ForecastDayModel
import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.presentation.detail.viewmodel.DetailViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun DetailScree(
    modifier: Modifier = Modifier,
    query: String?,
    detailViewModel: DetailViewModel = getViewModel(),
    onBackPressed: () -> Unit
) {
    LaunchedEffect(Unit) {
        detailViewModel.getWeatherDetail(query)
    }
    val state = detailViewModel.uiState
    DetailBody(
        modifier = modifier,
        weatherDetailModel = state.weatherDetailModel,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBody(
    modifier: Modifier,
    weatherDetailModel: WeatherDetailModel?,
    onBackPressed: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        topBar = {
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .height(48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Text(
                    text = weatherDetailModel?.locationModel?.name ?: String(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }, content = { padding ->
            ConstraintLayout(
                modifier
                    .fillMaxSize()
                    .padding(
                        top = 55.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = padding.calculateBottomPadding()
                    )

            ) {
                val (time, current, conditionImage, conditionTemp, conditionText, forecast) = createRefs()
                Text(
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                    },
                    text = weatherDetailModel?.locationModel?.localtime ?: String(),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    modifier = Modifier.constrainAs(current) {
                        start.linkTo(parent.start)
                        top.linkTo(time.bottom, 16.dp)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                    },
                    text = stringResource(id = R.string.label_today),
                    style = MaterialTheme.typography.titleLarge
                )

                AsyncImage(
                    modifier = Modifier
                        .constrainAs(conditionImage) {
                            start.linkTo(parent.start, 16.dp)
                            top.linkTo(current.bottom, 16.dp)
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                        }
                        .size(50.dp),
                    model = "https:${weatherDetailModel?.currentModel?.conditionModel?.icon}",
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.constrainAs(conditionText) {
                        start.linkTo(conditionImage.end)
                        top.linkTo(conditionImage.top)
                        bottom.linkTo(conditionImage.bottom)
                        width = Dimension.wrapContent
                    },
                    text = weatherDetailModel?.currentModel?.conditionModel?.text ?: String(),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    modifier = Modifier.constrainAs(conditionTemp) {
                        start.linkTo(conditionText.end)
                        end.linkTo(parent.end)
                        top.linkTo(conditionText.top)
                        bottom.linkTo(conditionText.bottom)
                        width = Dimension.wrapContent
                    },
                    text = "${weatherDetailModel?.currentModel?.tempC ?: "0"} °C",
                    style = MaterialTheme.typography.titleMedium
                )

                LazyRow(
                    modifier = Modifier.constrainAs(forecast) {
                        top.linkTo(conditionImage.bottom, 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                ) {
                    items(weatherDetailModel?.forecastModel?.forecastDayModel?.size ?: 0) { index ->
                        DayItem(weatherDetailModel?.forecastModel?.forecastDayModel?.get(index))
                    }
                }
            }
        })
}

@Composable
fun DayItem(forecastDayModel: ForecastDayModel?) {
    Card(
        modifier = Modifier
            .height(250.dp)
            .width(200.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onSecondary)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = forecastDayModel?.date ?: String(),
                style = MaterialTheme.typography.bodySmall
            )
            AsyncImage(
                modifier = Modifier.size(50.dp),
                model = "https:${forecastDayModel?.dayModel?.conditionModel?.icon}",
                contentDescription = null
            )

            Text(
                text = forecastDayModel?.dayModel?.conditionModel?.text ?: String(),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${forecastDayModel?.dayModel?.avgTempC ?: "0"} °C",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
