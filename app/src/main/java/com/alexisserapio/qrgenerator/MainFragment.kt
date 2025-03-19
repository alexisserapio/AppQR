package com.alexisserapio.qrgenerator

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.alexisserapio.qrgenerator.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var cameraPermissionGranted = false
    private var contactsPermissionGranted = false

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){permissions ->
        cameraPermissionGranted = permissions[Manifest.permission.CAMERA] == true
        contactsPermissionGranted = permissions[Manifest.permission.WRITE_CONTACTS] == true

        if (cameraPermissionGranted && contactsPermissionGranted){
            actionPermissionGranted(currentActionType)
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)||shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                //Le puedo dar razones adicionales
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.ADTitle)
                    .setMessage(R.string.ADMessage)
                    .setPositiveButton(R.string.ADPButton){_,_ ->
                        updateOrRequestPermission(currentActionType)
                    }
                    .setNegativeButton(R.string.ADNButton){dialog,_ ->
                        dialog.dismiss()
                        requireActivity().finish()
                    }
                    .create()
                    .show()
            }else{
                //Me nego el permiso permanentemente
                Toast.makeText(
                    requireContext(),
                    R.string.PermaBan,
                    Toast.LENGTH_SHORT

                ).show()
            }
        }

    }

    private var currentActionType: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCamara.setOnClickListener{
            updateOrRequestPermission(0)
        }

        binding.ivVcard.setOnClickListener {
            updateOrRequestPermission(1)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateOrRequestPermission(actiontype: Int){
        currentActionType = actiontype
        cameraPermissionGranted = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        contactsPermissionGranted = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED

            if(!cameraPermissionGranted || !contactsPermissionGranted){
                //Pedimos el permiso
                permissionLauncher.launch(arrayOf(
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.CAMERA
                ))

            }else{
                //Tenemos el permiso
                actionPermissionGranted(actiontype)

            }


    }

    private fun actionPermissionGranted(actiontype: Int){
        if(actiontype == 0){
            findNavController().navigate(R.id.action_mainFragment_to_scannerFragment)
        }else if (actiontype == 1){
            findNavController().navigate(R.id.action_mainFragment_to_formFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}