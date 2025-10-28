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
    fun recibirDanyo(danyo: Float){
        val k = 0.5f
        val danyoRecibido = danyo / (1 + k * ln( ((1+ defensa).toDouble()) )).toFloat()
        vida = (vida - danyoRecibido).coerceAtLeast(0f)
    }

    fun atacar(): Float{
        var k = 0.35f
        val ataqueBase = (ataqueMin..ataqueMax).random()
        val danyo = ataqueBase * (1 + k * ln((1 + ataqueBase).toDouble())).toFloat()
        return danyo
    }

    fun estaVivo(): Boolean = vida > 0
}