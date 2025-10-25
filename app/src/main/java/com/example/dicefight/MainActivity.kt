package com.example.dicefight

import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dicefight.ui.theme.MoradoOscuro
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    var vidaMob by remember { mutableStateOf(20f) }
    var vidaPlayer by remember { mutableStateOf(20f) }
    var ataque by remember { mutableStateOf(1) }
    val vidaMax = 20f

    val porcentajeVidaMob = (vidaMob/vidaMax).coerceIn(0f,1f)
    val porcentajeVidaPlayer = (vidaPlayer/vidaMax).coerceIn(0f,1f)

    val vidaAnimacionMob by animateFloatAsState(
        targetValue = porcentajeVidaMob,
        animationSpec = tween(durationMillis = 500)
    )


    val vidaAnimacionPlayer by animateFloatAsState(
        targetValue = porcentajeVidaPlayer,
        animationSpec = tween(durationMillis = 500)
    )

    val scope = rememberCoroutineScope()

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
                porcentajeVida = vidaAnimacionMob,
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
            BarraDeVida(
                porcentajeVida = vidaAnimacionPlayer
            )
            AnimacionTirarDado(CargarDados())
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

@Composable
fun AnimacionTirarDado(imagesDado: List<Int>){
    var dadoActual by remember { mutableStateOf(0)}
    var isLanzando by remember { mutableStateOf(false) }
    var resultadoDado by remember { mutableStateOf(0) }
    var lanzarDado by remember { mutableStateOf(false) }

    LaunchedEffect(lanzarDado) {
        if (lanzarDado){
            isLanzando = true
            repeat(15){
                dadoActual = (dadoActual + 1) % imagesDado.size
                delay(80)
            }
            resultadoDado = (0 until imagesDado.size).random()
            lanzarDado = false
            isLanzando = false
        }

    }

    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            painter = painterResource(id = imagesDado[if (isLanzando) dadoActual else resultadoDado]),
            contentDescription = "Cara del dado",
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = !isLanzando){
                    lanzarDado = true
                }
        )
    }

}

fun CargarDados(): List<Int> = listOf(
    R.drawable.dado_1,
    R.drawable.dado_2,
    R.drawable.dado_3,
    R.drawable.dado_4,
    R.drawable.dado_5,
    R.drawable.dado_6
)