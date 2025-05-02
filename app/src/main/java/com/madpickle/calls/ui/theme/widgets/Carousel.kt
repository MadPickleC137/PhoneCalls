package com.madpickle.calls.ui.theme.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.madpickle.calls.ui.theme.ContentPadding
import com.madpickle.calls.ui.theme.PaddingItem
import kotlin.math.abs

@Composable
fun Carousel(
    count: Int,
    modifier: Modifier,
    contentWidth: Dp,
    contentHeight: Dp,
    content: @Composable (parentModifier: Modifier, index: Int) -> Unit
) {
    val listState = rememberLazyListState(count / 2)

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val halfRowWidth = constraints.maxWidth / 2
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(ContentPadding),
            horizontalArrangement = Arrangement.spacedBy(PaddingItem),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                count = count,
                itemContent = { globalIndex ->
                    val scale by remember {
                        derivedStateOf {
                            val currentItem = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == globalIndex } ?: return@derivedStateOf 0.85f

                            (1f - minOf(1f, abs(currentItem.offset + (currentItem.size / 2) - halfRowWidth).toFloat() / halfRowWidth) * 0.25f)
                        }
                    }
                    content(
                        Modifier
                            .width(contentWidth)
                            .height(contentHeight)
                            .scale(scale)
                            .zIndex(scale * 10),
                        globalIndex % count
                    )
                }
            )
        }
    }
}