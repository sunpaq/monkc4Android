/*
use the java() macro like this:
------------------------------------------------------------------
#include "MCJNI.h"
#define java(class, type, name, ...) jni(com_yourcompany_app, class, type, name, __VA_ARGS__)

java(BENativeRenderer, void, resize, jint width, jint height)
{
	onResizeScreen(width, height);
}

java(BENativeRenderer, void, step, voida)
{
	onUpdate(0,0,0);
	onDraw();
}
-------------------------------------------------------------------
please config the package name in java() macro according to your project settings
example: com.oreisoft.beengine -> com_oreisoft_beengine

the macro need at least one argument
if the function have no argument use 'voida' as placeholder

each JNI function have two hidden arguments
1. JNIEnv* env
2. jobject obj
you can use 'env' & 'obj' access the JVM environment and Java object proxy
*/

#ifdef __ANDROID__

#include <jni.h>

#define JavaStringFromCString(cstr) (*env)->NewStringUTF(env, cstr)
#define CStringFromJavaString(jstr) (*env)->GetStringUTFChars(env, jstr, 0)
#define CStringRelease(jstr, cstr)  (*env)->ReleaseStringUTFChars(env, jstr, cstr)

#ifndef voida
#define voida void* voidarg
#endif

//JNI helper macro
#define jni(package, class, type, name, ...) JNIEXPORT type JNICALL Java_##package##_##class##_##name(JNIEnv* env, jobject obj, __VA_ARGS__)

//Copy this line into your JNI implementation C file. and change the package name!
#define java(class, type, name, ...) jni(com_oreisoft_beengine, class, type, name, __VA_ARGS__)

#endif
