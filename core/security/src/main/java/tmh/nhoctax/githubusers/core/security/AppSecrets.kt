package tmh.nhoctax.githubusers.core.security

import timber.log.Timber
import javax.inject.Inject

interface AppSecrets {
    val certificatePinnerKey: String
}

internal class AppSecretsImpl @Inject constructor() : AppSecrets {
    override val certificatePinnerKey: String
        get() = try {
            val tlsPublicKey = retrieveCertificatePinnerKey()
            Timber.d("Certificate Pinner Key: $tlsPublicKey")
            tlsPublicKey
        } catch (e: UnsatisfiedLinkError) {
            // Safe fallback during JVM unit tests which don't load native libraries easily
            ""
        }

    private external fun retrieveCertificatePinnerKey(): String

    companion object {
        init {
            try {
                System.loadLibrary("security_secrets")
            } catch (e: UnsatisfiedLinkError) {
                // Ignore during local JVM tests
            }
        }
    }
}
