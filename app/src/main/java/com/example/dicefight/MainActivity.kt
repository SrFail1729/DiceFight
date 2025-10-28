package com.example.dicefight

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
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
    var indiceMob by remember { mutableStateOf(0) }
    var mob = Miscelanea.monstruos[indiceMob]
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

    val scope = rememberCoroutineScope()

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

                //Siguiete mob con delay
                scope.launch {
                    delay(800)
                    indiceMob++
                    if (indiceMob < Miscelanea.monstruos.size){
                        mob = Miscelanea.monstruos[indiceMob]
                        vidaMob = mob.vida
                    }
                }
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

    var animacionInicial by remember { mutableStateOf(0)}
    var isLanzando by remember { mutableStateOf(false) }

    //Creamos la animación
    val animiacion by animateIntAsState(
        targetValue = animacionInicial, //Valor final al que tiene que llegar la animación
        animationSpec = tween(
            durationMillis = 1000, // Duración de la animación
            easing = LinearEasing // Mantine la velocidad constante
        ),
        finishedListener = {
            isLanzando = false
            val valorDado = (animacionInicial % imagesDado.size) + 1 // Primero con el modulo nos aseguramos de que el valor este dentro del indice y luego +1 lo "convierte" en un numero real del 1 al 6
            onResultado(valorDado)
        }
    )

    val caraActual = animiacion % imagesDado.size


    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            painter = painterResource(id = imagesDado[caraActual]),
            contentDescription = "Cara del dado",
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = !isLanzando) {
                    if (!isLanzando){
                        isLanzando = true
                        val vueltas = 12 + (1..6).random() // Número de vueltas que dará nuestro dado, le agregamo un poco de aletoriedad para dar sensación de lanzar dados
                        animacionInicial += vueltas
                    }
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
            LaunchedEffect(mob) {
                heroe.curacionEntreNivel()
            }
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
