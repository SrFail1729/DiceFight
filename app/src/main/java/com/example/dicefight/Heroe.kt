package com.example.dicefight

import kotlin.math.ln

class Heroe(
    var vida: Float,
    var defensa: Int,
    var ataqueBonus: Int = 0,
    val vivo: Boolean = true
) {
    var vidaMaxima: Float = vida

    fun recibirDanyo(danyo: Int){
        val k = 0.5f
        val danyoRecibido = danyo / (1 + k * ln( ((1+ defensa).toDouble()) )).toFloat()
        vida = (vida - danyoRecibido).coerceAtLeast(0f)
    }

    fun atacar(valorDado: Int): Int{
        return ataqueBonus + valorDado
    }

    fun estaVivo(): Boolean = vida > 0

    fun curacionEntreNivel() {
        vida +=  (vidaMaxima - vida) * 0.53f
    }

    fun objetoObtenido(objeto: Int){
        when(objeto){
            1 -> vidaMaxima += vidaMaxima * 0.5f
            2 -> vida = vidaMaxima
            3 -> ataqueBonus += 3
            4 -> defensa += 3
        }
    }
}