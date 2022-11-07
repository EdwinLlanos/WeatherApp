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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.weather.app.R
import com.weather.app.domain.model.ForecastDayModel
import com.weather.app.domain.model.WeatherDetailModel
import com.weather.app.framework.state.ScreenState
import com.weather.app.presentation.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun DetailScree(
    modifier: Modifier = Modifier,
    query: String?,
    detailViewModel: DetailViewModel = getViewModel(),
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        detailViewModel.getWeatherDetail(query)
    }
    val state = detailViewModel.uiState
    if (state.screenState == ScreenState.Error && state.errorMessage != null) {
        val errorMessage = stringResource(id = state.errorMessage)
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = errorMessage)
            }
        }
    }
    DetailBody(
        modifier = modifier,
        weatherDetailModel = state.weatherDetailModel,
        snackbarHostState = snackbarHostState,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBody(
    modifier: Modifier,
    weatherDetailModel: WeatherDetailModel?,
    snackbarHostState: SnackbarHostState,
    onBackPressed: () -> Unit
) {
    val showPlaceholder = weatherDetailModel == null
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
                    color = Color.White,
                    modifier = Modifier.placeholder(
                        visible = showPlaceholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    )
                )
            }
        }, content = { padding ->
            ConstraintLayout(
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = 55.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = padding.calculateBottomPadding()
                    )

            ) {
                val (time, current, conditionImage, conditionTemp, conditionText, forecast, snackbarHost) = createRefs()
                Text(
                    modifier = Modifier
                        .constrainAs(time) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            width = Dimension.wrapContent
                        }
                        .placeholder(
                            visible = showPlaceholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        ),
                    text = weatherDetailModel?.locationModel?.localtime ?: String(),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    modifier = Modifier
                        .constrainAs(current) {
                            start.linkTo(parent.start)
                            top.linkTo(time.bottom, 16.dp)
                            end.linkTo(parent.end)
                            width = Dimension.wrapContent
                        }
                        .placeholder(
                            visible = showPlaceholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        ),
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
                        .size(50.dp)
                        .placeholder(
                            visible = showPlaceholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        ),
                    model = "https:${weatherDetailModel?.currentModel?.conditionModel?.icon}",
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .constrainAs(conditionText) {
                            start.linkTo(conditionImage.end)
                            top.linkTo(conditionImage.top)
                            bottom.linkTo(conditionImage.bottom)
                            width = Dimension.wrapContent
                        }
                        .placeholder(
                            visible = showPlaceholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        ),
                    text = weatherDetailModel?.currentModel?.conditionModel?.text ?: String(),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    modifier = Modifier
                        .constrainAs(conditionTemp) {
                            start.linkTo(conditionText.end)
                            end.linkTo(parent.end)
                            top.linkTo(conditionText.top)
                            bottom.linkTo(conditionText.bottom)
                            width = Dimension.wrapContent
                        }
                        .placeholder(
                            visible = showPlaceholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            highlight = PlaceholderHighlight.shimmer(
                                highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        ),
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
                    items(weatherDetailModel?.forecastModel?.forecastDayModel?.size ?: 3) { index ->
                        DayItem(
                            weatherDetailModel?.forecastModel?.forecastDayModel?.get(index),
                            showPlaceholder
                        )
                    }
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.constrainAs(snackbarHost) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        })
}

@Composable
fun DayItem(forecastDayModel: ForecastDayModel?, showPlaceholder: Boolean) {
    Card(
        modifier = Modifier
            .height(250.dp)
            .width(200.dp)
            .padding(8.dp)
            .placeholder(
                visible = showPlaceholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )
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
