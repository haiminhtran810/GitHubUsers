package tmh.nhoctax.githubusers.core.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AppSecretsTest {

    @Test
    fun testCertificatePinnerKeyRetrieval() {
        val appSecrets = AppSecretsImpl()
        val key = appSecrets.CertificatePinnerKey
        assertNotNull(key)
        assertEquals("sha256/g8YgKdfHlH7dG5Wq69EclO...placeholder", key)
    }
}
