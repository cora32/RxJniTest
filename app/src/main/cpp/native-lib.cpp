#include <jni.h>
#include <string>
#include <iostream>

extern "C"
JNIEXPORT jstring

JNICALL
Java_org_iskopasi_rxjnitest_model_Repo_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_org_iskopasi_rxjnitest_model_Repo_stringFromJNI2(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++22";
    return env->NewStringUTF(hello.c_str());
}

const char *cache[100];

extern "C"
JNIEXPORT void
JNICALL
Java_org_iskopasi_rxjnitest_model_Repo_jniCacheData(
        JNIEnv *env,
        jobject, /* this */
        jobjectArray stringArray) {
    int i = 0;
    int size = env->GetArrayLength(stringArray);
    std::cout << "JNI: Item Java_org_iskopasi_rxjnitest_model_Repo_jniCacheData" << std::endl;
    for (i = 0; i < size; i++) {
        jstring string = (jstring) env->GetObjectArrayElement(stringArray, i);
        const char *rawString = env->GetStringUTFChars(string, 0);
        cache[i] = rawString;

        std::cout << "JNI: Item " << cache[i] << std::endl;

        env->ReleaseStringUTFChars(string, rawString);
    }
}
