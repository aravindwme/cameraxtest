package com.docsboilerp
import android.Manifest
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

class CustomView(context: Context) : FrameLayout(context), LifecycleObserver {

  private var camera: Camera? = null
  private var preview: Preview? = null
  private var viewFinder: PreviewView = PreviewView(context)
  private var cameraProvider: ProcessCameraProvider? = null
  val requiredPermissions = arrayOf(Manifest.permission.CAMERA)

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

    override fun onAttachedToWindow() {
      super.onAttachedToWindow()
       if (allPermissionsGranted()) {
         viewFinder.post { setupCamera() }
       } else {
           ActivityCompat.requestPermissions(
               getActivity(), requiredPermissions, 42)
       }

    }

     //context vs basecontext doubtful
     private fun allPermissionsGranted() = requiredPermissions.all {
       ContextCompat.checkSelfPermission(
           baseContext, it) == PackageManager.PERMISSION_GRANTED
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
}