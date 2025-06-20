package br.com.alura.projeto_web_api.app.ui

import androidx.compose.ui.semantics.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.alura.projeto_web_api.app.data.BottomSheetContent // Importe seu modelo
import br.com.alura.projeto_web_api.databinding.BottomSheetBenefitsBinding // ViewBinding gerado
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BenefitsBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBenefitsBinding? = null
    private val binding get() = _binding!!

    private var bottomSheetData: BottomSheetContent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera os dados passados como argumentos
        arguments?.let {
            bottomSheetData = it.getParcelable(ARG_BOTTOM_SHEET_DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBenefitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetData?.let { data ->
            binding.tvBottomSheetTitle.text = data.title
            // Formata a lista de benefícios em uma única string com marcadores
            val benefitsText = data.benefits.joinToString(separator = "\n") { "• $it" }
            binding.tvBottomSheetBenefitsContent.text = benefitsText
        } ?: run {
            // Fallback se os dados não forem passados
            binding.tvBottomSheetTitle.text = "Benefícios"
            binding.tvBottomSheetBenefitsContent.text = "Nenhum benefício para exibir."
        }

        binding.btnBottomSheetClose.setOnClickListener {
            dismiss() // Fecha o BottomSheet
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita memory leaks com ViewBinding
    }

    companion object {
        const val TAG = "BenefitsBottomSheetDialog"
        private const val ARG_BOTTOM_SHEET_DATA = "bottom_sheet_data"

        /**
         * Cria uma nova instância do BenefitsBottomSheetDialog com os dados necessários.
         * @param data O conteúdo a ser exibido no BottomSheet.
         */
        fun newInstance(data: BottomSheetContent): BenefitsBottomSheetDialog {
            val args = Bundle().apply {
                putParcelable(ARG_BOTTOM_SHEET_DATA, data)
            }
            val fragment = BenefitsBottomSheetDialog()
            fragment.arguments = args
            return fragment
        }
    }
}