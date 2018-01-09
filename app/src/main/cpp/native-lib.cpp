#include <jni.h>
#include <string>
#include <iostream>

#define LOG_TAG "JNI_TAG"
#       ifdef ANDROID
// LOGS ANDROID
#           include <android/log.h>

#           define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,__VA_ARGS__)
#           define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , LOG_TAG,__VA_ARGS__)
#           define LOGI(...) __android_log_print(ANDROID_LOG_INFO   , LOG_TAG,__VA_ARGS__)
#           define LOGW(...) __android_log_print(ANDROID_LOG_WARN   , LOG_TAG,__VA_ARGS__)
#           define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG,__VA_ARGS__)
#           define LOGSIMPLE(...)
#       else
// LOGS NO ANDROID
#           include <stdio.h>
#           define LOGV(...) printf("  ");printf(__VA_ARGS__); printf("\t -  <%s> \n", LOG_TAG);
#           define LOGD(...) printf("  ");printf(__VA_ARGS__); printf("\t -  <%s> \n", LOG_TAG);
#           define LOGI(...) printf("  ");printf(__VA_ARGS__); printf("\t -  <%s> \n", LOG_TAG);
#           define LOGW(...) printf("  * Warning: "); printf(__VA_ARGS__); printf("\t -  <%s> \n", LOG_TAG);
#           define LOGE(...) printf("  *** Error:  ");printf(__VA_ARGS__); printf("\t -  <%s> \n", LOG_TAG);
#           define LOGSIMPLE(...) printf(" ");printf(__VA_ARGS__);
#       endif // ANDROID

//const char *cache[100];
//const std::string *cache = new std::string[128];
char **cache = new char *[128];

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
Java_org_iskopasi_rxjnitest_model_Repo_stringFromJNICache(
        JNIEnv *env,
        jobject /* this */,
        jint index) {
    char *addr1 = cache[0];
    char *addr2 = cache[1];
    char *addr3 = cache[index];

    char log[256];
    sprintf(log, "0: %x  1: %x  %d: %x", addr1, addr2, index, addr3);
    __android_log_print(ANDROID_LOG_ERROR, (const char *) cache[index], " -- from cache");
    __android_log_print(ANDROID_LOG_ERROR, log, " -- addresses");
    return env->NewStringUTF((const char *) cache[index]);
}

extern "C"
JNIEXPORT void
JNICALL
Java_org_iskopasi_rxjnitest_model_Repo_jniCacheData(
        JNIEnv *env,
        jobject, /* this */
        jobjectArray data) {
    int size = env->GetArrayLength(data);
    for (int i = 0; i < size; i++) {
        jstring string = (jstring) env->GetObjectArrayElement(data, i);
        const char *rawString = env->GetStringUTFChars(string, 0);
        cache[i] = (char *) rawString;
//        env->ReleaseStringUTFChars(string, rawString);

        __android_log_print(ANDROID_LOG_ERROR, cache[i], " ITEM!");
    }
}
