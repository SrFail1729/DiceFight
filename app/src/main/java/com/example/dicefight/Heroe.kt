package com.example.dicefight

class Heroe(
    var vida: Float,
    var defensa: Int,
    var ataqueBonus: Int = 0,
    val vivo: Boolean = true
) {
    var vidaMaxima: Float = vida

    fun recibirDanyo(danyo: Int){
        val danyoRecibido = (danyo - defensa).coerceAtLeast(1)
        vida = (vida - danyoRecibido).coerceAtLeast(0f)
    }

    fun atacar(valorDado: Int): Int{
        return ataqueBonus + valorDado
    }

    fun estaVivo(): Boolean = vida > 0

    fun objetoObtenido(objeto: Int){
        when(objeto){
            1 -> vidaMaxima += 5
            2 -> vida = vidaMaxima
            3 -> ataqueBonus + 2
            4 -> defensa + 1
        }
    }
}