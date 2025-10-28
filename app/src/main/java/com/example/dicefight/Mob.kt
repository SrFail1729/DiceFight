package com.example.dicefight

import kotlin.math.ln


data class Mob(
    val nombre: String,
    val ataqueMin: Int,
    val ataqueMax: Int,
    val defensa: Int,
    val image: Int,
    var vida: Float,
    val vivo: Boolean
) {
    fun recibirDanyo(danyo: Int){
        val k = 0.7f
        val danyoRecibido = danyo / (1 + k * ln( ((1+ defensa).toDouble()) )).toFloat()
        vida = (vida - danyoRecibido).coerceAtLeast(0f)
    }

    fun atacar(): Int{
        return (ataqueMin..ataqueMax).random()
    }

    fun estaVivo(): Boolean = vida > 0
}