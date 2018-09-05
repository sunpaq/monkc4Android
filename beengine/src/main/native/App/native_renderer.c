#include "monkc_export.h"
#include "beengine_export.h"

#include "BEEngineCAPI.h"

#define java(clsname, type, name, ...) jni(com_oreisoft_beengine, clsname, type, name, __VA_ARGS__)

java(BENativeRenderer, void, initialGL, voida)
{
    const char* gles_version = (const char*)glGetString(GL_VERSION);
    const char* glsl_version = (const char*)glGetString(GL_SHADING_LANGUAGE_VERSION);
    debug_log("GLSL version is %s\n", glsl_version);
    if (strstr(gles_version, "OpenGL ES 3.")) {
        onTearDownGL();
        onSetupGL(1080, 1766);
        printf("[not a error] setup OpenGL ES 3");
    } else {
        printf("Unsupported OpenGL ES version");
        exit(0);
    }
}

//java(void, loadModelBegin);
//java(void, loadModelFinish);

java(BENativeRenderer, void, openFile, jstring name)
{
    const char* f = CStringFromJavaString(name);
    onOpenFile(f);
    CStringRelease(name, f);
    error_log("[not a error] JNI openFile called");
}

java(BENativeRenderer, void, resize, jint width, jint height)
{
    onResizeScreen(width, height);
    error_log("[not a error] JNI resize called");
}

java(BENativeRenderer, void, render, voida)
{
    onUpdate(0,0,0,0);
    onDraw();
}

java(BENativeRenderer, void, setAssetManager, jobject man)
{
    MCFileSetAssetManager(AAssetManager_fromJava(env, man));
}

java(BENativeRenderer, void, onGestureScale, jfloat scale)
{
    onGesturePinch(scale);
}

java(BENativeRenderer, void, onGestureScroll, jdouble x, jdouble y)
{
    onGesturePan(x, y);
}

java(BENativeRenderer, void, cacheTextureNamed, jstring name)
{
    const char* cname = CStringFromJavaString(name);
    MCTextureCache* tcache = MCTextureCache_shared(0);
    MCTexture* mtex = MCTextureCache_findTextureNamed(tcache, cname);
    if (mtex == null) {
        mtex = MCTexture_initWithFileName(new(MCTexture), cname);
        MCTextureCache_cacheTextureNamed(tcache, mtex, cname);
    }
    CStringRelease(name, cname);
}

java(BENativeRenderer, void, setCameraAutoRotation, jboolean autorotate)
{
    setCameraAutoRotation(autorotate);
}

java(BENativeRenderer, void, setDoesDrawWireFrame, jboolean wiremode)
{
    setDoesDrawWireFrame(wiremode);
}

java(BENativeRenderer, void, cameraTranslate, jfloat x, jfloat y, jfloat z, jboolean incremental)
{
    cameraTranslate(x, y, z, incremental);
}

java(BENativeRenderer, void, cameraDistanceScale, jdouble scale, jdouble min, jdouble max)
{
    cameraDistanceScale(scale, min, max);
}

//external fun metaCall(classname: String, methodname: String)
java(BENativeRenderer, void, metaCall, jstring methodname)
{
    const char* str = CStringFromJavaString(methodname);
    //msg(director, str, 0);
}

