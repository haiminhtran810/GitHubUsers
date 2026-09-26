#include <jni.h>
#include <string>

// Simple XOR obfuscation to hide the string from static analysis tools like `strings`
std::string decodeObfuscatedString(const char *encoded, int length, char key) {
    std::string decoded = "";
    for (int i = 0; i < length; i++) {
        decoded += (char) (encoded[i] ^ key);
    }
    return decoded;
}

extern "C" {

JNIEXPORT jstring JNICALL
Java_tmh_nhoctax_githubusers_core_security_AppSecretsImpl_retrieveCertificatePinnerKey(
        JNIEnv *env,
        jobject /* this */) {

    // Use XOR obfuscation to hide Original String
    // We XOR each character with a key (e.g., 0x5A) to generate this byte array:
    const char obfuscatedKey[] = {
            0x09, 0x68, 0x16, 0x0F, 0x13, 0x38, 0x2B, 0x6E, 0x23, 0x0F, 0x3D, 0x6F, 0x2D, 0x71, 0x17, 0x03, 0x38, 0x30, 0x6F, 0x16, 0x00, 0x15, 0x0D, 0x1B, 0x00, 0x1B, 0x20, 0x3B, 0x3F, 0x14, 0x1D, 0x10, 0x63, 0x28, 0x0E, 0x0E, 0x39, 0x6E, 0x1D, 0x30, 0x2C, 0x18, 0x0B, 0x67
    };
    int length = sizeof(obfuscatedKey) / sizeof(obfuscatedKey[0]);
    char xorKey = 0x5A; // The hidden XOR key

    // Reconstruct the key at runtime
    std::string secretKey = decodeObfuscatedString(obfuscatedKey, length, xorKey);

    return env->NewStringUTF(secretKey.c_str());
}

}
