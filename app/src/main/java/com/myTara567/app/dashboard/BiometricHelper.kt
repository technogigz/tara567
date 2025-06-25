import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.myTara567.app.new_ui_update.EnterMPINActivity
import java.util.concurrent.Executor

class BiometricHelper(private val context: Context) {

    fun isBiometricAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val biometricManager = BiometricManager.from(context)
            val availability = biometricManager.canAuthenticate()

            return when (availability) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    true // Biometric is available and ready to use
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    showToast("No biometric hardware detected.")
                    false
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    showToast("Biometric hardware is unavailable.")
                    false
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    showToast("No biometric data is enrolled.")
                    false
                }
                else -> {
                    showToast("Biometric authentication is not available.")
                    false
                }
            }
        } else {
            showToast("Biometric authentication is not supported on this device.")
            return false
        }
    }

    // Perform biometric authentication
    fun authenticate(callback: BiometricPrompt.AuthenticationCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!isBiometricAvailable()) {
                return // If biometric is not available, return early
            }

            val executor: Executor = ContextCompat.getMainExecutor(context)
            val biometricPrompt = BiometricPrompt(
                context as EnterMPINActivity, executor, callback
            )

            // Build the BiometricPrompt
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate using your biometric credential")
                .setDescription("Place your finger on the fingerprint sensor or use face recognition.")
                .setNegativeButtonText("Cancel")
                .build()

            // Start the authentication process
            biometricPrompt.authenticate(promptInfo)
        } else {
            showToast("Biometric authentication is not supported on this device.")
        }
    }

    // Helper method to show a toast message
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
