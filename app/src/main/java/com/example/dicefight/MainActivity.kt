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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.dicefight.ui.theme.MoradoOscuro
import kotlinx.coroutines.delay

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
fun VistaApp() {
    val mob = Miscelanea.monstruos[0]
    var vidaMob by remember { mutableStateOf(mob.vida) }

    val heroe = remember { Heroe(20f, defensa = 0) }
    var vidaPlayer by remember { mutableStateOf(heroe.vida) }

    val vidaMaxMob = remember { mob.vida }
    var vidaPlayerMax = remember { heroe.vidaMaxima }

    val vidaAnimacionMob by animateFloatAsState(
        targetValue = (vidaMob/vidaMaxMob).coerceIn(0f,1f),
        animationSpec = tween(durationMillis = 500)
    )


    val vidaAnimacionPlayer by animateFloatAsState(
        targetValue = (vidaPlayer/vidaPlayerMax).coerceIn(0f,1f),
        animationSpec = tween(durationMillis = 500)
    )

    Column(
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxSize()
    ){

        ZonaMob(
            mob,
            heroe,
            vidaAnimacionMob,
            loot = {resultado ->
                heroe.objetoObtenido(resultado)
                vidaPlayerMax = heroe.vidaMaxima
                vidaPlayer = heroe.vida
            },
            modifier = Modifier.weight(2f))

       ZonaPlayer(
           vidaAnimacionPlayer, modifier = Modifier.weight(1f),
           onTirarDado = {
               resultado ->
               val danyo = heroe.atacar(resultado)
               mob.recibirDanyo(danyo)
               vidaMob = mob.vida

               if(mob.estaVivo()){
                   val danyoMob = mob.atacar()
                   heroe.recibirDanyo(danyoMob)
                   vidaPlayer = heroe.vida
               }

           }
       )
    }
}

@Composable
fun BarraDeVida(
    porcentajeVida: Float,
    modifier: Modifier = Modifier
){
    Box (
        modifier = modifier
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
fun AnimacionTirarDado(
    imagesDado: List<Int>,
    onResultado:(Int)-> Unit){

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

            //Da al exterior el resultado de la tirada
            onResultado(resultadoDado + 1)

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
                .clickable(enabled = !isLanzando) {
                    lanzarDado = true
                }
        )
    }

}

@Composable
fun AnimacionItem(
    items: List<Int>,
    onResultado:(Int)-> Unit){
    var itemActual by remember { mutableStateOf(0)}
    var mostrandoAnimacion by remember { mutableStateOf(true) }
    var resultadoItem by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {

        repeat(15){
            itemActual = (itemActual + 1) % items.size
            delay(80)
        }
        resultadoItem = (0 until items.size).random()

        //Da al exterior el resultado de la tirada
        onResultado(resultadoItem + 1)


        mostrandoAnimacion = false

    }
    val imagen = if (mostrandoAnimacion) items[itemActual] else items[resultadoItem]

    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            painter = painterResource(id = imagen),
            contentDescription = "Item",
            modifier = Modifier.fillMaxSize()
        )
    }

}

@Composable
fun ZonaMob(
    mob: Mob,
    heroe: Heroe,
    vidaAnimacionMob: Float,
    loot: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .background(MoradoOscuro),
        horizontalAlignment = Alignment.CenterHorizontally

    ){

        BarraDeVida(
            porcentajeVida = vidaAnimacionMob,
        )

        if (mob.estaVivo()){
            Image(
                painter = painterResource(mob.image),
                contentDescription = mob.nombre,
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
        }else{
            AnimacionItem(Miscelanea.objetos,
                onResultado = {
                    resultado -> loot(resultado)
                }

            )
        }

    }
}

@Composable
fun ZonaPlayer(
    vidaAnimacionPlayer: Float,
    modifier: Modifier = Modifier,
    onTirarDado:(Int) -> Unit,

){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BarraDeVida(
            porcentajeVida = vidaAnimacionPlayer
        )
        AnimacionTirarDado(
            Miscelanea.dado,
            onResultado = {
                resultado -> onTirarDado(resultado)
            }
        )
    }
}
