#include <jni.h>
#include <string.h>
#include <stdio.h>

#ifdef __cplusplus
extern "C" {
#endif

static jclass contextClass;
static jclass signatureClass;
static jclass packageNameClass;
static jclass packageInfoClass;



#define  PAY_SIGN_SECRET  "itsfunplay"
const char* SHOW_SIGN_SECRET = "SHOW_SNAILVR_AUTHENTICATION";
const char* WHALEYVR_SIGN_SECRET = "WHALEYVR_SNAILVR_AUTHENTICATION";
const char* TEST_USER_HISTORY_SIGN_SECRET = "5d40190e25c04495bb920abe34d16a98caeb903a56e14b1d8c578f6ae8834c77750d76d4c21545e99abb952fb13603e7c8620099e2e74d3aa6863a4fe091d03f";
const char* USER_HISTORY_SIGN_SECRET = "e0dfa6491c3e4976b96feb3ad93112dc06e219b08f7f49148c2cf78ea451a3e1468dcd62bdcd435d9ce290290c8bdba68ce17124c6f94ff0a53bbf46110d26ca";
const char* BI_SIGN_SECRET = "eOfNyUQr1mBSb3ijDYh3GqVSq5lJZVeKJX81Us8ZyQcFPpDWOOK6Uu5WinKgHNJv";
const char* TEST_WHALEYVR_THIRD_PAY_SECRET = "5d40190e25c04495bb920abe34d16a98caeb903a56e14b1d8c578f6ae8834c77750d76d4c21545e99abb952fb13603e7c8620099e2e74d3aa6863a4fe091w2q8";
const char* WHALEYVR_THIRD_PAY_SECRET = "hvxhb2jr2t726x22156v5xdv476lvdd3tf3hn081d133np52r725tf2z132zvzp4j8p5567x32l3pnr7przntl5d14db602l97px23dx1f9hv37v0zl1x69b3hb4r56f";
/*
    根据context对象,获取签名字符串
*/
const char* getSignString(JNIEnv *env,jobject contextObject) {
    jmethodID getPackageManagerId = (env)->GetMethodID(contextClass, "getPackageManager","()Landroid/content/pm/PackageManager;");
    jmethodID getPackageNameId = (env)->GetMethodID(contextClass, "getPackageName","()Ljava/lang/String;");
    jmethodID signToStringId = (env)->GetMethodID(signatureClass, "toCharsString","()Ljava/lang/String;");
    jmethodID getPackageInfoId = (env)->GetMethodID(packageNameClass, "getPackageInfo","(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jobject packageManagerObject =  (env)->CallObjectMethod(contextObject, getPackageManagerId);
    jstring packNameString =  (jstring)(env)->CallObjectMethod(contextObject, getPackageNameId);
    jobject packageInfoObject = (env)->CallObjectMethod(packageManagerObject, getPackageInfoId,packNameString, 64);
    jfieldID signaturefieldID =(env)->GetFieldID(packageInfoClass,"signatures", "[Landroid/content/pm/Signature;");
    jobjectArray signatureArray = (jobjectArray)(env)->GetObjectField(packageInfoObject, signaturefieldID);
    jobject signatureObject =  (env)->GetObjectArrayElement(signatureArray,0);
    return (env)->GetStringUTFChars((jstring)(env)->CallObjectMethod(signatureObject, signToStringId),0);
}

int isApiKey(JNIEnv *env,jobject contextObject){
   return strcmp(getSignString(env,contextObject),"30820245308201aea00302010202045498dd17300d06092a864886f70d01010505003066310b300906035504061302434e310e300c060355040813054368696e613111300f060355040713085368616e676861693110300e060355040a1307536e61696c56523110300e060355040b1307536e61696c56523110300e06035504031307536e61696c56523020170d3134313232333033313031355a180f33313130303232313033313031355a3066310b300906035504061302434e310e300c060355040813054368696e613111300f060355040713085368616e676861693110300e060355040a1307536e61696c56523110300e060355040b1307536e61696c56523110300e06035504031307536e61696c565230819f300d06092a864886f70d010101050003818d0030818902818100966201d3ca4d7ffc99e2d514a208e5eae4a0b0bc0b7aceec6c3fcba46ee01dbfa3df02789cd32a333f9990e55b408e2ca7876e07033955687f5453989fd07c60e6cd36f88d22d5af495c991bcf5a56090d23441e550d110294539d6064a30dc0585bd808d6284fde87b1d140218ed1757fd6c5ed53e1082e661b090070f411790203010001300d06092a864886f70d0101050500038181003e17f7bbba1db694d20c4b43f5270acf9e89905593fd42599096bd57de44b0ebd68e6de7238be342a7d33af8b6f16dd95d68bc770aaeeed6ee3f1340d0505b0763f624686455b40bcd9b763d714f208c3aa11f4195471032b3c91863ea937d8d3c893d90e1639783e5bd72e144fedfe8a70f427334d681cd8d38da19427906a5");
}

jstring Java_com_whaley_lib_sign_Sign_getPaySign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0)//签名一致  返回合法的 api key，否则返回错误
    {
       return (env)->NewStringUTF(PAY_SIGN_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}

jstring Java_com_whaley_lib_sign_Sign_getShowSign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0)//签名一致  返回合法的 api key，否则返回错误
    {
       return (env)->NewStringUTF(SHOW_SIGN_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}

jstring Java_com_whaley_lib_sign_Sign_getTestUserHistorySign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0)
    {
       return (env)->NewStringUTF(TEST_USER_HISTORY_SIGN_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}

jstring Java_com_whaley_lib_sign_Sign_getUserHistorySign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0){
       return (env)->NewStringUTF(USER_HISTORY_SIGN_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}

jstring Java_com_whaley_lib_sign_Sign_getWhaleyvrThirdPaySign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0){
       return (env)->NewStringUTF(WHALEYVR_THIRD_PAY_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}

jstring Java_com_whaley_lib_sign_Sign_getTestWhaleyvrThirdPaySign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0){
       return (env)->NewStringUTF(TEST_WHALEYVR_THIRD_PAY_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}

jstring Java_com_whaley_lib_sign_Sign_getBISign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0){
       return (env)->NewStringUTF(BI_SIGN_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}

jstring Java_com_whaley_lib_sign_Sign_getWhaleyvrSign(
        JNIEnv *env, jobject thiz, jobject contextObject) {
    if(isApiKey(env,contextObject)==0){
       return (env)->NewStringUTF(WHALEYVR_SIGN_SECRET);
    }else
    {
       return (env)->NewStringUTF("error");
    }
}


/**
    利用OnLoad钩子,初始化需要用到的Class类.
*/
JNIEXPORT jint JNICALL JNI_OnLoad (JavaVM* vm,void* reserved){

     JNIEnv* env = NULL;
     jint result=-1;
     if(vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
       return result;

     contextClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/Context"));
     signatureClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/pm/Signature"));
     packageNameClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/pm/PackageManager"));
     packageInfoClass = (jclass)env->NewGlobalRef((env)->FindClass("android/content/pm/PackageInfo"));

     return JNI_VERSION_1_4;
 }

#ifdef __cplusplus
}
#endif