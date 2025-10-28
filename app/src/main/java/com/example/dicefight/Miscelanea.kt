package com.example.dicefight

object Miscelanea {

    //Imágenes del dado
    val dado = listOf(
        R.drawable.dado_1,
        R.drawable.dado_2,
        R.drawable.dado_3,
        R.drawable.dado_4,
        R.drawable.dado_5,
        R.drawable.dado_6
    )

    //Lista de monstruos
    val monstruos = listOf<Mob>(
        Mob(
            nombre = "Crimsonette",
            ataqueMin = 1,
            ataqueMax = 5,
            defensa = 0,
            image = R.drawable.monster1,
            vida = 15f,
            vivo = true
        ),
        Mob(
            nombre = "Lurvion",
            ataqueMin = 2,
            ataqueMax = 7,
            defensa = 2,
            image = R.drawable.monster2,
            vida = 15f,
            vivo = true
        )
    )

    //Lista de objetos
    val objetos = listOf(
        R.drawable.heartv3,
        R.drawable.potion,
        R.drawable.sword,
        R.drawable.shield
    )
}