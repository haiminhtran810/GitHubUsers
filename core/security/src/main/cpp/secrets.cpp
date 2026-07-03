#include <jni.h>
#include <string>

// Simple XOR obfuscation to hide the string from static analysis tools like `strings`
std::string decodeObfuscatedString(const char* encoded, int length, char key) {
    std::string decoded = "";
    for (int i = 0; i < length; i++) {
        decoded += (char)(encoded[i] ^ key);
    }
    return decoded;
}

extern "C" {

JNIEXPORT jstring JNICALL
Java_tmh_nhoctax_githubusers_core_security_AppSecretsImpl_retrieveCertificatePinnerKey(
        JNIEnv* env,
        jobject /* this */) {

    // Use XOR obfuscation to hide Original String
    // We XOR each character with a key (e.g., 0x5A) to generate this byte array:
    const char obfuscatedKey[] = {
            0x28, 0x36, 0x31, 0x1B, 0x33, 0x10, 0x1F, 0x30,
            0x1B, 0x2D, 0x28, 0x6F, 0x0F, 0x09, 0x2C, 0x39,
            0x39, 0x00, 0x68, 0x14, 0x36, 0x16, 0x20, 0x20,
            0x6D, 0x3F, 0x36, 0x00, 0x1F, 0x0E, 0x15, 0x3B,
            0x38, 0x09, 0x34, 0x31, 0x08, 0x2C, 0x11, 0x3E,
            0x35, 0x2D, 0x6A, 0x67
    };
    int length = sizeof(obfuscatedKey) / sizeof(obfuscatedKey[0]);
    char xorKey = 0x5A; // The hidden XOR key

    // Reconstruct the key at runtime
    std::string secretKey = decodeObfuscatedString(obfuscatedKey, length, xorKey);

    return env->NewStringUTF(secretKey.c_str());
}

}
