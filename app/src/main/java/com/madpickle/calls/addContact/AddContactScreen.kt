package com.madpickle.calls.addContact

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import com.madpickle.calls.R
import com.madpickle.calls.data.ContactDraft
import com.madpickle.calls.data.ImageType
import com.madpickle.calls.data.getResByType
import com.madpickle.calls.ui.theme.ContentPadding
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.cardItem
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class AddContactScreen(private val draft: ContactDraft? = null) : Screen {
    private val icons = ImageType.entries

    private val imageSize = 144.dp

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val initialImage = icons.indexOfFirst { it.name == draft?.image?.name }
        val pagerState = rememberPagerState(
            pageCount = { icons.count() },
            initialPage = if (initialImage < 0) 0 else initialImage,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingItem),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(ContentPadding)
        ) {
            item {
                Box(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.fillMaxWidth().zIndex(2f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            enabled = pagerState.canScrollBackward
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arror_start),
                                modifier = Modifier.size(50.dp).padding(ContentPadding),
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.size(imageSize + 40.dp))
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            enabled = pagerState.canScrollForward
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arror_end),
                                modifier = Modifier.size(50.dp).padding(ContentPadding),
                                contentDescription = null
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.align(Alignment.Center),
                        pageSpacing = ContentPadding,
                        snapPosition = SnapPosition.Start
                    ) { index ->
                        val icon = icons[index]
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(imageSize)
                                    .graphicsLayer {
                                        val pageOffset = (
                                                (pagerState.currentPage - index) + pagerState
                                                    .currentPageOffsetFraction
                                                ).absoluteValue

                                        alpha = lerp(
                                            start = 0.3f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                    }
                                    .background(MaterialTheme.cardItem, CircleShape),
                                alignment = Alignment.Center,
                                painter = painterResource(icon.getResByType()),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
            }
        }
    }
}