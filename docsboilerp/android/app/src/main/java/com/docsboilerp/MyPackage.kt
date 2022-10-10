// replace with your package
package com.docsboilerp

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.bridge.NativeModule

class MyPackage : ReactPackage {
    
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return emptyList()
  }
  override fun createViewManagers(
      reactContext: ReactApplicationContext
  ) = listOf(MyViewManager(reactContext))
}