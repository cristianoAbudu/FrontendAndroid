package com.jovemtranquilao.frontendandroid.dto

data class ColaboradorDTO(
     val id : Int,
     val nome : String,
     val senha : String,
     val score : String,
     val chefe : ColaboradorDTO?,
)