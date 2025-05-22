package com.madpickle.calls.data

import com.madpickle.calls.R

enum class ImageType {
    Default, Tiger, Lion,
    Dog, Cat, Rat,
    Owl, Shark, Elephant,
    Girafe, Croco, Bear,
    Beer, Bull, Gorilla,
    Hiena, Leopard, Kengeru,
    Turtle
}

fun ImageType.getResByType(): Int {
    return when(this) {
        ImageType.Default -> R.drawable.user
        ImageType.Tiger -> R.drawable.tiger
        ImageType.Lion -> R.drawable.lion
        ImageType.Dog -> R.drawable.dog
        ImageType.Cat -> R.drawable.cat
        ImageType.Rat -> R.drawable.rat
        ImageType.Owl -> R.drawable.owl
        ImageType.Shark -> R.drawable.shark
        ImageType.Elephant -> R.drawable.elephant
        ImageType.Girafe -> R.drawable.girafe
        ImageType.Croco -> R.drawable.croco
        ImageType.Bear -> R.drawable.bear
        ImageType.Beer -> R.drawable.beer
        ImageType.Bull -> R.drawable.bull
        ImageType.Gorilla -> R.drawable.gorilla
        ImageType.Hiena -> R.drawable.hiena
        ImageType.Leopard -> R.drawable.leopard
        ImageType.Kengeru -> R.drawable.kengeru
        ImageType.Turtle -> R.drawable.turtle
    }
}
