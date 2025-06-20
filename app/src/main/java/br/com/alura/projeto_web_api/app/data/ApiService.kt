package br.com.alura.projeto_web_api.app.data

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("apresentacao")
    suspend fun getPresentationData(): Response<PresentationApiResponse>
    // usa-se response <ex> para ter acesso a mais detalhes da resposta
    // e para um tratamento de erro mais completo
    // esse 'suspend' indica que é uma função de suspensão para ser chamada de uma coroutine
}