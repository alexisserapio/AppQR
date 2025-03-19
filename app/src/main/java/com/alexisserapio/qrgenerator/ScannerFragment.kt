package com.alexisserapio.qrgenerator

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.alexisserapio.qrgenerator.databinding.FragmentScannerBinding
import java.net.MalformedURLException
import java.net.URL

class ScannerFragment : Fragment() {

    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cbvScanner.decodeContinuous { result ->
            val scannedText = result.text

            // Intentar detectar si es una URL
            try {
                val url = URL(scannedText)

                // Si es una URL válida, abrirla en el navegador
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url.toString())
                }
                startActivity(intent)

            } catch (e: MalformedURLException) {
                if (isVCard(scannedText)) {
                    val name = extractNameFromVCard(scannedText)
                    val phone = extractPhoneFromVCard(scannedText)
                    val email = extractEmailFromVCard(scannedText)

                    Toast.makeText(requireContext(), getString(R.string.VCardDetected), Toast.LENGTH_LONG).show()
                    addContactToPhone(name, phone, email)

                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.NotValidScan),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } finally {
                binding.cbvScanner.pause()
                findNavController().navigate(R.id.action_scannerFragment_to_mainFragment)
            }
        }
    }

    // Función para verificar si el texto es una vCard
    private fun isVCard(text: String): Boolean {
        return text.contains("BEGIN:VCARD") && text.contains("END:VCARD")
    }

    private fun extractNameFromVCard(vCard: String): String {
        val regex = "FN:(.*)".toRegex()
        val matchResult = regex.find(vCard)
        return matchResult?.groups?.get(1)?.value ?: "Desconocido"
    }

    private fun extractPhoneFromVCard(vCard: String): String {
        val regex = "TEL:(.*)".toRegex()
        val matchResult = regex.find(vCard)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    private fun extractEmailFromVCard(vCard: String): String {
        val regex = "EMAIL:(.*)".toRegex()
        val matchResult = regex.find(vCard)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    private fun addContactToPhone(name: String, phone: String, email: String) {
        val contentResolver = requireContext().contentResolver

        val rawContactUri = contentResolver.insert(android.provider.ContactsContract.RawContacts.CONTENT_URI, ContentValues())
        val rawContactId = rawContactUri?.lastPathSegment?.toLong() ?: return

        val nameValues = ContentValues().apply {
            put(android.provider.ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(android.provider.ContactsContract.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            put(android.provider.ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        }
        contentResolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, nameValues)

        if (phone.isNotEmpty()) {
            val phoneValues = ContentValues().apply {
                put(android.provider.ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                put(android.provider.ContactsContract.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                put(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                put(android.provider.ContactsContract.CommonDataKinds.Phone.TYPE, android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            }
            contentResolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, phoneValues)
        }

        if (email.isNotEmpty()) {
            val emailValues = ContentValues().apply {
                put(android.provider.ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                put(android.provider.ContactsContract.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                put(android.provider.ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                put(android.provider.ContactsContract.CommonDataKinds.Email.TYPE, android.provider.ContactsContract.CommonDataKinds.Email.TYPE_WORK)
            }
            contentResolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, emailValues)
        }

        Toast.makeText(requireContext(), getString(R.string.ContactoDetectado, name, phone, email), Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        binding.cbvScanner.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.cbvScanner.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}