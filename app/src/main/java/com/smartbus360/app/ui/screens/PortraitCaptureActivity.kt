//package com.smartbus360.app.ui.screens
//
//import com.journeyapps.barcodescanner.CaptureActivity
//
//class PortraitCaptureActivity : CaptureActivity()


package com.smartbus360.app.ui.screens

import android.os.Bundle
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class PortraitCaptureActivity : CaptureActivity() {

    private var barcodeView: DecoratedBarcodeView? = null

    override fun onResume() {
        super.onResume()

        // ZXing's barcode view ID inside its layout
        val id = resources.getIdentifier("zxing_barcode_surface", "id", packageName)

        try {
            barcodeView = findViewById(id)
            barcodeView?.resume()
        } catch (_: Exception) { }
    }

    override fun onPause() {
        super.onPause()

        // ⭐ FIX: stop camera so BottomSheet buttons become clickable
        try {
            barcodeView?.pause()
        } catch (_: Exception) { }
    }
}
