package com.example.dicefight

import android.os.Bundle
import android.text.Layout
import android.text.style.BackgroundColorSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dicefight.ui.theme.DiceFightTheme
import com.example.dicefight.ui.theme.MoradoOscuro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceFightApp()
        }
    }
}

@Preview
@Composable
fun DiceFightApp() {
    VistaApp()
}


@Composable
fun VistaApp(modifier: Modifier = Modifier) {

    var vida by remember { mutableStateOf(20f) }
    var ataque by remember { mutableStateOf(1) }
    val vidaMax = 20f
    val porcentajeVida = (vida/vidaMax).coerceIn(0f,1f)

    val vidaAnimacion by animateFloatAsState(
        targetValue = porcentajeVida,
        animationSpec = tween(durationMillis = 500)
    )

    Column(
        modifier = Modifier.background(Color.Blue)
            .fillMaxSize()
    ){
        Box (
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                .background(MoradoOscuro)

        ){

            BarraDeVida(
                porcentajeVida = vidaAnimacion,
            )

            Image(
                painter = painterResource(R.drawable.monster1),
                contentDescription = "Monstruo1",
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.Black)
        ){
            Image(
                painter = painterResource(CargarDados()[ataque-1]),
                contentDescription = "Dado",
                modifier = Modifier.fillMaxSize()
                    .clickable(onClick = {
                        ataque = (1..6).random()
                        if (vida > 0){
                            vida -= ataque
                        }
                    })
            )
        }
    }
}

@Composable
fun BarraDeVida(
    porcentajeVida: Float,
    modifier: Modifier = Modifier
){
    Box (
        modifier = Modifier
            .height(20.dp)
            .fillMaxWidth()
            .background(Color.DarkGray)
    ){
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(porcentajeVida)
                .background(Color.Red)
        )
    }
}

fun CargarDados(): List<Int> = listOf(
    R.drawable.dice_1,
    R.drawable.dice_2,
    R.drawable.dice_3,
    R.drawable.dice_4,
    R.drawable.dice_5,
    R.drawable.dice_6
)