package br.com.alura.projeto_web_api.data

import com.google.gson.annotations.SerializedName

// essa é a raiz da resposta da API
data class PresentationApiResponse(
    @SerializedName("presentationFlow")
    val presentationFlow: PresentationFlow
)

data class PresentationFlow(
    @SerializedName("title")
    val title: String,

    @SerializedName("taxTitle")
    val taxTitle: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("lines")
    val lines: List<LineItem>,

    @SerializedName("primaryButton")
    val primaryButton: ButtonAction,

    @SerializedName("secondaryButton")
    val secondaryButton: ButtonAction,

    @SerializedName("modal")
    val modal: ModalInfo
)

data class LineItem(
    @SerializedName("icon")
    val icon: String, // isso é caminho do ícone drawable futuro

    @SerializedName("title")
    val title: String,

    @SerializedName("separator")
    val separator: Boolean
)

data class ButtonAction(
    @SerializedName("title")
    val title: String,

    @SerializedName("action")
    val action: String // esse pode ser usado para lógica/ação depois
)

data class ModalInfo(
    @SerializedName("title")
    val title: String,

    @SerializedName("subtitle")
    val subtitle: String,

    @SerializedName("notification")
    val notification: NotificationInfo,

    @SerializedName("copyPasteText")
    val copyPasteText: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("primaryButton")
    val primaryButton: ButtonAction,

    @SerializedName("secondaryButton")
    val secondaryButton: ButtonAction
)

data class NotificationInfo(
    @SerializedName("type")
    val type: String, // ex: "SUCCESS_THEME"

    @SerializedName("title")
    val title: String
)