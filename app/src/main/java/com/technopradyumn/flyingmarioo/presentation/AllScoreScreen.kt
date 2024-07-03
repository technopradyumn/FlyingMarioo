package com.technopradyumn.flyingmarioo.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.technopradyumn.flyingmarioo.data.Score

@Composable
fun AllScoreScreen(navHostController: NavHostController) {
    val gameViewModel: GameViewModel = viewModel()
    val scores by gameViewModel.scores.collectAsState()

    val highestScore = if (scores.isNotEmpty()) scores.maxOf { it.scoreValue } else 0
    val secondHighestScore = findNthHighestScore(scores, 2)
    val thirdHighestScore = findNthHighestScore(scores, 3)

    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navHostController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "All Score",
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        if (scores.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No Scores Available", fontSize = 20.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    scores.sortedByDescending { it.scoreValue },
                    key = { _, score -> score.id }) { index, score ->
                    var itemAppeared by remember { mutableStateOf(false) }
                    LaunchedEffect(key1 = score) { // Trigger when a new score item appears
                        itemAppeared = true
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        visible = itemAppeared,
                        enter = slideInVertically(initialOffsetY = { it * 2 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it * 2 }) + fadeOut()
                    ) {
                        Crossfade(targetState = score) { currentScore ->
                            ScoreItemWithAnimation(
                                currentScore,
                                highestScore,
                                secondHighestScore,
                                thirdHighestScore,
                                index
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ScoreItemWithAnimation(
    score: Score,
    highestScore: Int,
    secondHighestScore: Int,
    thirdHighestScore: Int,
    index: Int
) {
    val backgroundColor = when (score.scoreValue) {
        highestScore -> Color(0xFF673AB7) // Deep Purple (Primary)
        secondHighestScore -> Color(0xFF00BCD4) // Cyan (Secondary)
        thirdHighestScore -> Color(0xFF4CAF50) // Green (Tertiary)
        else -> Color.White
    }

    val textColor = when (score.scoreValue) {
        highestScore, secondHighestScore,thirdHighestScore -> Color.White
        else -> Color.Black
    }

    val rankText = when (score.scoreValue) {
        highestScore -> "1st"
        secondHighestScore -> "2nd"
        thirdHighestScore -> "3rd"
        else -> ""
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                if (rankText.isNotEmpty()) {
                    Text(
                        text = "Rank: $rankText",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                }

                Text(
                    text = "Score: ${score.scoreValue}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    text = score.gameOverTime,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (rankText.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rank Icon",
                    modifier = Modifier
                        .size(44.dp),
                    tint = textColor
                )
            }

        }
    }
}

private fun findNthHighestScore(scores: List<Score>, n: Int): Int {
    return if (scores.size >= n) {
        scores.sortedByDescending { it.scoreValue }[n - 1].scoreValue
    } else {
        0
    }
}