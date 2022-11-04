package com.weather.app.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.weather.app.R
import com.weather.app.domain.model.WeatherModel
import com.weather.app.presentation.search.viewmodel.SearchViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = getViewModel(),
    onNavigateToDetail: (String) -> Unit
) {
    val textToSearch = remember { mutableStateOf(String()) }
    val hasFocus = remember { mutableStateOf(false) }
    val state = searchViewModel.uiState
    SearchBody(
        modifier = modifier,
        listWeatherModel = state.listWeatherModel,
        textToSearch = textToSearch.value,
        hasFocus = hasFocus.value,
        onSearchWord = {
            textToSearch.value = it
            searchViewModel.search(it)
        },
        onHasFocus = {
            hasFocus.value = it
        },
        onItemClicked = onNavigateToDetail
    )
}

@Composable
fun SearchBody(
    modifier: Modifier = Modifier,
    listWeatherModel: List<WeatherModel>,
    textToSearch: String,
    hasFocus: Boolean,
    onSearchWord: (wordOrPhrase: String) -> Unit,
    onHasFocus: (isFocused: Boolean) -> Unit,
    onItemClicked: (String) -> Unit
) {
    Column(modifier = modifier) {
        SearchViewTextField(
            textToSearch = textToSearch,
            hasFocus = hasFocus,
            onTextChange = onSearchWord,
            onFocusChange = onHasFocus
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            LazyColumn(modifier.padding(horizontal = 16.dp)) {
                items(listWeatherModel.size) { index ->
                    ItemRow(weatherModel = listWeatherModel[index], onItemClicked = onItemClicked)
                }
            }
        }
    }
}

@Composable
fun ItemRow(
    modifier: Modifier = Modifier,
    weatherModel: WeatherModel,
    onItemClicked: (String) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onItemClicked(weatherModel.name) }) {
        val (name, country) = createRefs()
        Text(
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom, 2.dp)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
            text = weatherModel.name, style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier.constrainAs(country) {
                top.linkTo(name.bottom)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
            text = weatherModel.country, style = MaterialTheme.typography.bodySmall
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SearchViewTextField(
    modifier: Modifier = Modifier,
    textToSearch: String,
    hasFocus: Boolean,
    onTextChange: (wordPhrase: String) -> Unit,
    onFocusChange: (hasFocus: Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = textToSearch,
        onValueChange = {
            onTextChange(it)
        },
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .height(58.dp)
            .padding(horizontal = 16.dp)
            .onFocusChanged { focusState ->
                when {
                    focusState.isFocused -> onFocusChange(true)
                    else -> onFocusChange(false)
                }
            }
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(Color.White),
        textStyle = MaterialTheme.typography.titleMedium,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hasFocus) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clickable {
                                onTextChange(String())
                                focusManager.clearFocus()
                            },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSecondary,
                            shape = MaterialTheme.shapes.large
                        )
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 8.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                    if (textToSearch.isEmpty()) Text(
                        text = stringResource(id = R.string.label_search),
                        style = MaterialTheme.typography.labelSmall
                    )
                    innerTextField()
                }
            }
        })
}
