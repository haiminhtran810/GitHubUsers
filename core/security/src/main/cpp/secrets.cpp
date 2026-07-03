#include <jni.h>
#include <string>

extern "C" {

JNIEXPORT jstring JNICALL
Java_tmh_nhoctax_githubusers_core_security_AppSecretsImpl_retrieveCertificatePinnerKey(
        JNIEnv* env,
        jobject /* this */) {
    // This is where we securely/obfuscatedly store our Certificate Pinner Key.
    // For now, returning a sample key that can be updated.
    std::string secretKey = "public tls key";
    return env->NewStringUTF(secretKey.c_str());
}

}
