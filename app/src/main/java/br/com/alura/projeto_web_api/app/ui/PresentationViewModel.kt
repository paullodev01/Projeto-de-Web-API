package br.com.alura.projeto_web_api.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.projeto_web_api.app.data.PresentationApiResponse
import br.com.alura.projeto_web_api.app.data.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

// isso representa os diferentes estados da UI
sealed class PresentationUiState {
    object Loading : PresentationUiState()
    data class Success(val data: PresentationApiResponse) : PresentationUiState()
    data class Error(val message: String) : PresentationUiState()
}

class PresentationViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService // vai obter a instância do serviço

    // esse mutableLiveData para o estado da UI, exposto como LiveData que não muda
    private val _uiState = MutableLiveData<PresentationUiState>()
    val uiState: LiveData<PresentationUiState> = _uiState

    // essa função é para buscar os dados quando o viewMode for inicializado

    fun fetchPresentationData() {
        _uiState.value = PresentationUiState.Loading // Define o estado como Loading
        viewModelScope.launch { // vai uma coroutine no escopo da viewModel
            try {
                val response = apiService.getPresentationData()
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        _uiState.postValue(PresentationUiState.Success(data))
                    } ?: run {
                        _uiState.postValue(PresentationUiState.Error("Resposta vazia da API."))
                    }
                } else {
                    // esse catch erros HTTP (ex: 404, 500)
                    _uiState.postValue(PresentationUiState.Error("Erro da API: ${response.code()} - ${response.message()}"))
                }
            } catch (e: IOException) {
                // esse erros de rede (ex: sem conexão, timeout)
                _uiState.postValue(PresentationUiState.Error("Erro de rede: ${e.message}"))
            } catch (e: Exception) {
                // e esse outros inesperados
                _uiState.postValue(PresentationUiState.Error("Erro desconhecido: ${e.message}"))
            }
        }

    }

}