package com.alexisserapio.qrgenerator

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alexisserapio.qrgenerator.databinding.FragmentFormBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix


class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ETName.addTextChangedListener(loginTextWatcher)
        binding.ETNumber.addTextChangedListener(loginTextWatcher)


        binding.buttonVerificar.setOnClickListener{
            fun esNombreValido(nombre: String): Boolean {
                val regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']+$".toRegex()
                return regex.matches(nombre)
            }
            if(!esNombreValido(binding.ETName.text.toString())){
                binding.ETName.error = getString(R.string.error_nombre)
                binding.ETName.requestFocus()
                return@setOnClickListener
            }

            if(binding.ETEmail.text.toString().isNotEmpty()){
                if(!Patterns.EMAIL_ADDRESS.matcher(binding.ETEmail.text.toString()).matches()){
                    binding.ETEmail.error = getString(R.string.error_email)
                    binding.ETEmail.requestFocus()
                    return@setOnClickListener
                }
            }

            if(binding.ETNumber.length() in 1..9){
                binding.ETNumber.error= getString(R.string.error_numero)
                binding.ETNumber.requestFocus()
                return@setOnClickListener
            }

            var name = binding.ETName.text.trim().toString()
            var phonenumber = binding.ETNumber.text.toString()
            var email = binding.ETEmail.text.trim().toString()

            val qrCodeBitmap = generateVcard(name, phonenumber, email)
            binding.buttonVerificar.visibility = View.INVISIBLE
            // Mostrar el código QR en el ImageView
            binding.imageViewQRCode.setImageBitmap(qrCodeBitmap)

        }

    }

    /*private fun generateVcard(name: String, phonenumber: String, email: String):String {
        return """
                    BEGIN:VCARD
                    VERSION:3.0
                    FN:$name
                    TEL:$phonenumber
                    EMAIL:$email
                    END:VCARD
                """.trimIndent()

    }*/

    private fun generateVcard(name: String, phonenumber: String, email: String): Bitmap {
        val vCardString = """
        BEGIN:VCARD
        VERSION:3.0
        FN:$name
        TEL:$phonenumber
        EMAIL:$email
        END:VCARD
        """.trimIndent()

        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            vCardString,
            BarcodeFormat.QR_CODE,
            500, 500 // Tamaño del QR
        )

        binding.imageViewQRCode.visibility = View.VISIBLE

        return Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565).apply {
            for (x in 0 until 500) {
                for (y in 0 until 500) {
                    setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
        }
    }

    private val loginTextWatcher=object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.buttonVerificar.isEnabled =
                binding.ETName.text.toString().trim().isNotEmpty() &&
                        binding.ETNumber.length()==10


        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Asignar el binding
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

}