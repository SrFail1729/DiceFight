package com.example.dicefight

class Heroe(
    var vidaMax: Float,
    var vida: Float,
    var defensa: Int,
    var dados: IntRange = (1..6),
    var ataqueBonus: Int = 0,
    val vivo: Boolean = true
) {

    fun lazarDado(): Int = dados.random()

    fun recibirDanyo(danyo: Int){
        val danyoRecibido = (danyo - defensa).coerceAtLeast(1)
        vida = (vida - danyoRecibido).coerceAtLeast(0f)
    }

    fun atacar(): Int{
        return ataqueBonus+lazarDado()
    }

    fun estaVivo(): Boolean = vida > 0
}