package com.jovemtranquilao.frontendandroid

import com.google.gson.annotations.SerializedName

// Uncomment the follow line if you're using the Kotlinx Serialization converter
// @Serializable
data class ColaboradorDTO(

    // Use @SerializedName(" ") for the Gson converter
    // @field:Json(name = " ") for the Moshi converter
    // @SerialName(" ") for the Kotlinx Serialization converter
    // @JsonProperty(" ") for the Jackson converter

    @SerializedName("id")
    var id: Int,

    @SerializedName("nome")
    var nome: String?,

    @SerializedName("senha")
    var senha: String?,

    @SerializedName("score")
    var score: String?,

    @SerializedName("chefe")
    var chefe: ColaboradorDTO?

)