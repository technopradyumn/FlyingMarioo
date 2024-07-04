package com.technopradyumn.flyingmarioo.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.technopradyumn.flyingmarioo.R
import com.technopradyumn.flyingmarioo.ui.theme.FlyingMariooTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay

enum class Screen {
    Home, PlayGame, Score, Premium,
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlyingMariooTheme {
                val navHostController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(navHostController = navHostController, innerPadding = innerPadding)
                }
            }
        }
    }
}

@Composable
fun Navigation(
    navHostController: NavHostController,
    innerPadding: androidx.compose.foundation.layout.PaddingValues
) {
    NavHost(navController = navHostController, startDestination = Screen.Home.name) {
        composable(Screen.PlayGame.name) {
            PlayGameScreen(navHostController = navHostController)
        }
        composable(Screen.Home.name) {
            HomeScreen(
                navHostController = navHostController, modifier = Modifier.padding(innerPadding)
            )
        }
        composable(Screen.Score.name) {
            AllScoreScreen(navHostController = navHostController)
        }
        composable(Screen.Premium.name) {
            PremiumFeaturesScreen(navHostController = navHostController)
        }

    }
}

@Composable
fun HomeScreen(navHostController: NavHostController, modifier: Modifier? = Modifier) {
    Column(
        modifier = modifier!!.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var imageAnimation by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(100)
            imageAnimation = true
        }

        AnimatedVisibility(
            visible = imageAnimation,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Image(
                painter = painterResource(id = R.drawable.supermarioo),
                contentDescription = "Super Mario",
                modifier = Modifier
                    .height(400.dp)
                    .width(400.dp)
            )
        }


        GoButton(navHostController = navHostController)

    }
}

@Composable
fun GoButton(navHostController: NavHostController) {
    var isLoadingGo by remember { mutableStateOf(false) }
    var goButtonAnimation by remember { mutableStateOf(false) }

    var isLoadingTop by remember { mutableStateOf(false) }
    var topButtonAnimation by remember { mutableStateOf(false) }

    var isLoadingPremium by remember { mutableStateOf(false) }
    var premiumButtonAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        goButtonAnimation = true
        delay(50)
        topButtonAnimation = true
        delay(50)
        premiumButtonAnimation = true
    }

    Column {
        AnimatedVisibility(
            visible = goButtonAnimation,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Button(
                onClick = {
                    isLoadingGo = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        navHostController.navigate(Screen.PlayGame.name)
                    }, 1000)
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
            ) {
                if (isLoadingGo) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .size(32.dp), color = Color.Blue
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.go_ic),
                        contentDescription = "Super Mario",
                        modifier = Modifier
                            .width(72.dp)
                            .height(72.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = topButtonAnimation,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Button(
                onClick = {
                    navHostController.navigate(Screen.Score.name)
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.score_ic),
                    contentDescription = "Super Mario",
                    modifier = Modifier
                        .width(72.dp)
                        .height(72.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        AnimatedVisibility(
            visible = premiumButtonAnimation,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Button(
                onClick = {
                    navHostController.navigate(Screen.Premium.name)
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.premium_ic),
                    contentDescription = "Super Mario",
                    modifier = Modifier
                        .width(72.dp)
                        .height(72.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navHostController = rememberNavController()
    HomeScreen(navHostController = navHostController, null)
}