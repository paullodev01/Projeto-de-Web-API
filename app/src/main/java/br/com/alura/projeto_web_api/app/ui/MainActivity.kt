package br.com.alura.projeto_web_api.app.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels // Para by viewModels()
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer // Para LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.projeto_web_api.R
import br.com.alura.projeto_web_api.app.data.PresentationApiResponse
import br.com.alura.projeto_web_api.databinding.ActivityMainBinding // Import do ViewBinding gerado

class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // ViewModel usando a delegação de propriedade 'by viewModels()'
    private val presentationViewModel: PresentationViewModel by viewModels()

    private lateinit var lineItemAdapter: LineItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupClickListeners() // Configura os listeners dos botões

        // Chama para buscar os dados quando a Activity é criada
        presentationViewModel.fetchPresentationData()
    }

    private fun setupRecyclerView() {
        lineItemAdapter = LineItemAdapter()
        binding.recyclerViewBeneficios.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = lineItemAdapter
            // Garante que o RecyclerView não tenha sua própria rolagem quando dentro de NestedScrollView
            // e que sua altura seja baseada no conteúdo.
            isNestedScrollingEnabled = false
        }
    }

    private fun observeViewModel() {
        presentationViewModel.uiState.observe(this, Observer { state ->
            when (state) {
                is PresentationUiState.Loading -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.tvErrorMessage.visibility = View.GONE
                    binding.clMainContentInternal.visibility = View.GONE
                    binding.llButtonsContainer.visibility = View.GONE
                }
                is PresentationUiState.Success -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.tvErrorMessage.visibility = View.GONE
                    binding.clMainContentInternal.visibility = View.VISIBLE
                    binding.llButtonsContainer.visibility = View.VISIBLE
                    populateUi(state.data)
                }
                is PresentationUiState.Error -> {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.clMainContentInternal.visibility = View.GONE
                    binding.llButtonsContainer.visibility = View.GONE // Ou mostrar, mas desabilitar os botões
                    binding.tvErrorMessage.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = state.message
                    Log.e("MainActivity", "Erro ao carregar dados: ${state.message}")
                }
            }
        })
    }

    private fun populateUi(data: PresentationApiResponse) {
        val flowData = data.presentationFlow // Acessa o objeto PresentationFlow dentro da resposta

        // Popula os TextFields principais
        binding.tvApiTitle.text = flowData.title
        binding.tvApiTaxTitle.text = flowData.taxTitle
        binding.tvApiDescription.text = flowData.description

        // Popula o RecyclerView
        // A API retorna uma lista de 'LineItem', que é o que o adapter espera.
        lineItemAdapter.submitList(flowData.lines)

        // Popula os botões
        flowData.primaryButton?.let { buttonData ->
            binding.btnPrimaryAction.text = buttonData.text
            binding.btnPrimaryAction.visibility = View.VISIBLE
            // Ação do botão primário será configurada em setupClickListeners
        } ?: run {
            binding.btnPrimaryAction.visibility = View.GONE
        }

        flowData.secondaryButton?.let { buttonData ->
            binding.btnSecondaryAction.text = buttonData.text
            binding.btnSecondaryAction.visibility = View.VISIBLE
            // Ação do botão secundário será configurada em setupClickListeners
        } ?: run {
            binding.btnSecondaryAction.visibility = View.GONE
        }

        // Você pode querer definir um estado padrão para os botões se a API não os enviar
        if (flowData.primaryButton == null && flowData.secondaryButton == null) {
            // Se nenhum botão for fornecido pela API, você pode esconder o container de botões
            // binding.llButtonsContainer.visibility = View.GONE;
            // Ou mostrar botões padrão com ações locais
        }
    }

    private fun setupClickListeners() {
        binding.btnPrimaryAction.setOnClickListener {
            // Ação para o botão primário
            // A API pode fornecer uma "action" ou "deeplink"
            // Por enquanto, vamos apenas mostrar um Toast
            val action = presentationViewModel.uiState.value?.let { state ->
                if (state is PresentationUiState.Success) {
                    state.data.presentationFlow.primaryButton?.action
                } else null
            }
            Toast.makeText(this, "Botão Primário clicado! Ação: ${action ?: "Nenhuma ação definida"}", Toast.LENGTH_SHORT).show()

            // Aqui você implementaria a navegação ou lógica baseada na 'action'
            // Ex: if (action == "OPEN_SIMULATION") { /* abrir tela de simulação */ }
        }

        binding.btnSecondaryAction.setOnClickListener {
            // Ação para o botão secundário
            val action = presentationViewModel.uiState.value?.let { state ->
                if (state is PresentationUiState.Success) {
                    state.data.presentationFlow.secondaryButton?.action
                } else null
            }
            Toast.makeText(this, "Botão Secundário clicado! Ação: ${action ?: "Nenhuma ação definida"}", Toast.LENGTH_SHORT).show()

            // Se a ação for "OPEN_BOTTOM_SHEET" (conforme o desafio), você abriria o BottomSheet aqui.
            if (action == "OPEN_BOTTOM_SHEET") { // Exemplo de verificação da ação
                // TODO: Chamar a lógica para abrir o BottomSheetDialog (Passo 5)
                Log.d("MainActivity", "Ação para abrir BottomSheet detectada.")
                // showBenefitsBottomSheet() // Você precisará criar esta função e o BottomSheet
            }
        }
    }

    // TODO: Criar a função para mostrar o BottomSheetDialog (Passo 5)
    // private fun showBenefitsBottomSheet() {
    //     val bottomSheet = BenefitsBottomSheetDialog() // Supondo que você crie esta classe
    //     bottomSheet.show(supportFragmentManager, BenefitsBottomSheetDialog.TAG)
    // }

}