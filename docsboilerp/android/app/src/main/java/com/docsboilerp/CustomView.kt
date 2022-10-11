package com.docsboilerp
import android.Manifest
import android.os.Build
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.widget.FrameLayout
import android.widget.TextView
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.util.Log
import com.facebook.react.uimanager.ThemedReactContext

class CustomView(context: ThemedReactContext) : FrameLayout(context), LifecycleObserver {

  private var preview: Preview? = null
  private var viewFinder: PreviewView = PreviewView(context)
  private var cameraProvider: ProcessCameraProvider? = null
  private val currentContext: ThemedReactContext = context


  init {
    // set padding and background color
    setPadding(16,16,16,16)
    //setBackgroundColor(Color.parseColor("#5FD3F3"))
    addView(viewFinder)
    // add default text view
    // addView(TextView(context).apply {
    //   text = "Welcome to Android Fragments with React Native."
    // })
  }

    private fun getActivity() : Activity {
        return currentContext.currentActivity!!
    }

    override fun onAttachedToWindow() {
      super.onAttachedToWindow()
       if (allPermissionsGranted()) {
         viewFinder.post { setupCamera() }
       } else {
           ActivityCompat.requestPermissions(
               getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
       }

    }

     //context vs basecontext doubtful
     private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
       ContextCompat.checkSelfPermission(
           context, it) == PackageManager.PERMISSION_GRANTED
     }


    private fun setupCamera() { 
       val cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity())
      
       cameraProviderFuture.addListener({
       // Used to bind the lifecycle of cameras to the lifecycle owner
       val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

       // Preview
       val preview = Preview.Builder()
          .build()
          .also {
              it.setSurfaceProvider(viewFinder.getSurfaceProvider())
          }

       // Select back camera as a default
       val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

       try {
           // Unbind use cases before rebinding
           cameraProvider.unbindAll()

           // Bind use cases to camera
           cameraProvider.bindToLifecycle(
               getActivity() as AppCompatActivity, cameraSelector, preview)

       } catch(exc: Exception) {
           Log.e(TAG, "Use case binding failed", exc)
       }

       }, ContextCompat.getMainExecutor(getActivity()))
    }

    companion object {
       private const val TAG = "CameraXApp"
       private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
       private const val REQUEST_CODE_PERMISSIONS = 10
       private val REQUIRED_PERMISSIONS =
           mutableListOf (
               Manifest.permission.CAMERA,
               Manifest.permission.RECORD_AUDIO
           ).apply {
               if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                   add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               }
           }.toTypedArray()
   }
}