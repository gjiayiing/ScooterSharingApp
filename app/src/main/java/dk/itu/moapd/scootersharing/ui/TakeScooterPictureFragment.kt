package dk.itu.moapd.scootersharing.ui

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.data.FILENAME_FORMAT
import dk.itu.moapd.scootersharing.data.REQUEST_CODE_PERMISSIONS
import dk.itu.moapd.scootersharing.data.REQUIRED_PERMISSIONS
import dk.itu.moapd.scootersharing.databinding.FragmentTakeScooterPictureBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [TakeScooterPictureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TakeScooterPictureFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private lateinit var cameraExecutors: ExecutorService
    private var imageUri: Uri? = null

    private lateinit var safeContext: Context
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: FragmentTakeScooterPictureBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun allPermissionsGranted() =

        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
        }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(safeContext, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
//                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTakeScooterPictureBinding.inflate(inflater, container, false)

        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_take_scooter_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        // Request camera permissions
        if (allPermissionsGranted()) {
            Toast.makeText(requireContext(), "We have permission", Toast.LENGTH_SHORT).show()
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        val camera_capture_button = view.findViewById<ImageButton>(R.id.camera_capture_button)
        // Setup the listener for take photo button
        binding.cameraCaptureButton.setOnClickListener{
            takePhoto()
        }
        binding.previewPicture.setOnClickListener{
            Toast.makeText(activity,imageUri.toString(), Toast.LENGTH_SHORT).show()
            if (imageUri != null) {
                //Put the value
                val frag = CreateScooterFragment()
                val bundle = Bundle()
                bundle.putString("uri", imageUri.toString())
                frag.arguments = bundle
                //Inflate the fragment
                val transaction =  parentFragmentManager.beginTransaction()
                transaction?.replace(R.id.navhostFragment, frag)
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
        }

    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also{ mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.createSurfaceProvider()
                    )
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            }catch (e:Exception){
                Log.d(TAG, "StartCamera Fail", e)
            }
        }, ContextCompat.getMainExecutor(safeContext))
    }

    private fun getOutputDirectory(): File{
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            mFile->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }
    private fun takePhoto(){
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
                .format(System.currentTimeMillis()) + ".jpg")

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOption, ContextCompat.getMainExecutor(safeContext), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                imageUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $imageUri"
                Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()

            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}