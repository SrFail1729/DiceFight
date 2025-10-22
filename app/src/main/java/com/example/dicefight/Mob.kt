package com.example.dicefight

data class Mob(
    val nombre: String,
    val ataqueMin: Int,
    val ataqueMax: Int,
    val defensa: Int,
    val vidaMax: Float,
    var vida: Float,
    val vivo: Boolean
) {
    fun recibirDanyo(danyo: Int){
        val danyoRecibido = (danyo - defensa).coerceAtLeast(1)
        vida = (vida - danyoRecibido).coerceAtLeast(0f)
    }

    fun atacar(): Int{
        return (ataqueMin..ataqueMax).random()
    }

    fun estaVivo(): Boolean = vida > 0
}