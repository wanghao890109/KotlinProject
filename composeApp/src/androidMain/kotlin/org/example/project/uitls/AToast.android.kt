package org.example.project.uitls

import android.widget.Toast
import org.example.project.AppContextHolder

actual object AToast {
    actual fun show(message: String) {
        Toast.makeText(AppContextHolder.context, message, Toast.LENGTH_SHORT).show()
    }
}