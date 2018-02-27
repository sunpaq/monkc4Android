#include <jni.h>
#include <GLES3/gl3.h>
#include "monkc.h"
#include "MCJNI.h"
#include "MCString.h"
#include "MC3DiOS.h"
#include "BEAssetsManager.h"
#include "BECubeTextureData.h"
#include "MCDirector.h"
#include "MCCube.h"

static MCDirector* director = null;
static BECubeTextureData* cubtex = null;

void onAppStart()
{
    if (cubtex == null) {
        const char* names[6] = {"right","left","top","bottom","back","front"};
        cubtex = BECubeTextureData_newWithFaces(names);
    }
}

void onRootViewLoad(void* rootview)
{
    //put the test code into Testbed.c
    //starttest();

#ifdef __APPLE__
    MCUIRegisterRootUIView(rootview);
#endif
}

void onOpenExternalFile(const char* filepath)
{
    MC3DModel* model = ff(new(MC3DModel), initWithFilePathColor, filepath,
                          (MCColorf){MCFloatF(1.0), MCFloatF(1.0), MCFloatF(1.0), MCFloatF(1.0)});
    ff(director->lastScene->rootnode, setAllVisible, false);
    ff(director, addModel, model);
}

//static void asyncReadSkybox()
//{
//    ff(director->lastScene, loadSkybox, 0);
//}

void openFile(const char* filename)
{
    //model
    MC3DModel* model = ff(new(MC3DModel), initWithFileNameColor, filename,
                          (MCColorf){MCFloatF(0.8), MCFloatF(0.8), MCFloatF(0.8), MCFloatF(1.0)});
    if (model) {
        debug_log("Create MC3DModel success:%s\n", model->name);

        MCDirector_addModel(director, model, MCFloatF(10.0));
        MCDirector_cameraFocusOn(director, MCVector4Make(0, -10 * 0.5, 0, 10 * 2.0));

    } else {
        error_log("Can not create MC3DModel:%s\n", filename);
        exit(-1);
        //throw(MC3DModel_ERROR);
    }
}

void openFileAndExitThread(void* arg)
{
//    try {
//        onOpenFile(filename);
//        ff(director->lastScene->rootnode, setAllVisible, true);
//    }catch(MC3DModel_ERROR) {
//        error_log("MC3DModel_ERROR occur exit the process!");
//    }finally{
//        exit(-1);
//    }

    MCString* filename = cast(MCString*, arg);

    openFile(filename->buff);
    ff(director->lastScene->rootnode, setAllVisible, true);

    release(filename);
    MCGLStopLoading();
    MCThread_exitWithStatus((void*)200);
}

void openFileAsync(const char* filename)
{
    MCGLStartLoading();//call in UI thread

    MCString* str = MCString_newWithCString(filename);
    ff(director->modelThread, initWithFPointerArgument, openFileAndExitThread, (void*)str);
    ff(director->modelThread, start, 0);

    //MCThread_joinThread(director->modelThread->tid);
}

void onReceiveMemoryWarning()
{
    error_log("Receive Memory Warning\n");
    MC3DScene* mainScene = director->lastScene;
    if (mainScene != null && mainScene->rootnode != null) {
        ff(mainScene->rootnode, cleanUnvisibleChild, 0);
    }
}

void onOpenFile(const char* filename)
{
    if (director == null) {
        onSetupGL(640, 480);
    }

    if (filename != null) {
        //if (director->lastScene->skyboxRef != null) {
            //ff(director->skyboxThread, initWithFPointerArgument, asyncReadSkybox, null);
            //ff(director->skyboxThread, start, 0);
        //}

        //openFileAsync(filename);
        openFile(filename);
    }
}

void onSetupGL(int windowWidth, int windowHeight)
{
    debug_log("onSetupGL called: width=%d height=%d\n", windowWidth, windowHeight);
    //MCLogTypeSet(MC_SILENT);

    if (director == null) {
        debug_log("onSetupGL create director\n");
        director = new(MCDirector);
        director->currentWidth = windowWidth;
        director->currentHeight = windowHeight;
        debug_log("onSetupGL director created\n");

        //scene1
        MC3DScene* mainScene = ff(new(MC3DScene), initWithWidthHeightDefaultShader,
                                  director->currentWidth, director->currentHeight);
        debug_log("onSetupGL main scene created current screen size: %dx%d\n", windowWidth, windowHeight);

//        mainScene->sky = getSkyboxOn();
//
//        if (cubtex != null && mainScene->skyboxShow) {
//            MCSkybox* skybox = MCSkybox_initWithCubeTexture(0, new(MCSkybox), cubtex, MCRatioMake(windowWidth, windowHeight));
//            mainScene->skyboxRef = skybox;
//        }

        mainScene->mainCamera->R_value = 20;
        mainScene->mainCamera->tht = 60;
        mainScene->mainCamera->fai = 45;

        superof(mainScene)->nextResponder = (MCObject*)director;

        ff(director, pushScene, mainScene);

        //kick off
        MCDirector_updateAll(director, 0);
        MCDirector_drawAll(director, 0);

        debug_log("onSetupGL main scene pushed into director\n");
    }
}

void onTearDownGL()
{
    release(director);
    director = null;
}

void onUpdate(double roll, double yaw, double pitch, double w)
{
    //printf("sensor data: roll=%f yaw=%f pitch=%f\n", roll, yaw, pitch);
    //MCLogTypeSet(MC_SILENT);
    if (director != null) {

//        if (computed(director->lastScene, isDrawSky)) {
//            if (director->currentWidth < director->currentHeight) {
//                MCSkyboxCamera_setAttitude(0, director->lastScene->skyboxRef->camera,
//                                           MCFloatF(roll*360), MCFloatF((pitch-1)*45));
//            }else{
//                MCSkyboxCamera_setAttitude(0, director->lastScene->skyboxRef->camera,
//                                           MCFloatF(pitch*360), MCFloatF((roll-1)*45));
//            }
//        }

        MCDirector_updateAll(director, 0);
    }
}

int onDraw()
{
    int fps = -1;
    if (director != null) {
        fps = MCDirector_drawAll(director, 0);
    }

    MCLogTypeSet(MC_DEBUG);
    return fps;
}

//gesture callback
void onGestureSwip()
{
    if (director != null && director->lastScene != null) {
        //MCDirector_popScene(0, director, 0);
    }
}

void onGesturePan(double x, double y)
{
    MCCamera* camera = director->lastScene->mainCamera;

    if (director != null && director->lastScene != null && camera != null) {
        double sign = camera->isReverseMovement == true? -1.0f : 1.0f;
        if (camera->isLockRotation == true) {
            double factor = 0.01;
            MCCamera_fucus(camera, MCFloatF(x*sign*factor), MCFloatF(y*sign*factor));
        }else{
            MCCamera_move(camera, MCFloatF(x*sign), MCFloatF(y*sign));
//            if (computed(director->lastScene, isDrawSky)) {
//                MCCamera* cam2 = superof(director->lastScene->skyboxRef->camera);
//                MCCamera_move(cam2, MCFloatF(x*sign / 5), MCFloatF(y*sign / 5));
//            }
        }
    }
}

static float pinch_scale = 1.0;
void onGesturePinch(double scale)
{
    pinch_scale *= scale;
    pinch_scale = MAX(0.1, MIN(pinch_scale, 100.0));

    MCCamera* camera = director->lastScene->mainCamera;
    if (director != null && director->lastScene != null && camera != null) {
        MCCamera_distanceScale(camera, MCFloatF(1.0/pinch_scale));
    }
}

void onResizeScreen(int windowWidth, int windowHeight)
{
    if (director != null) {
        ff(director, resizeAllScene, windowWidth, windowHeight);
    }
}

void onStartStopBtn(int startOrStop)
{
    if (director && director->lastScene) {
        director->lastScene->cameraLock = !startOrStop;
    }
}

//MC3DiOS_CameraLookAt,
//MC3DiOS_CameraLookAtDelta,
//MC3DiOS_CameraRadius,
//MC3DiOS_CameraRadiusDelta,
//MC3DiOS_CameraAngels,
//MC3DiOS_CameraAngelsDelta
void cameraCommand(MC3DiOS_CameraCmd* cmd)
{
    if (director != null && director->lastScene != null) {
        MCCamera* camera = computed(director, cameraHandler);
        MCCamera* cam2 = null;
//        if (computed(director->lastScene, isDrawSky)) {
//            MCSkyboxCamera* sbcam = director->lastScene->skyboxRef->camera;
//            cam2 = superof(sbcam);
//        }

        if (camera != null) {
            switch (cmd->type) {
                case MC3DiOS_CameraLookAt:
                    camera->lookat.x = cmd->lookatX;
                    camera->lookat.y = cmd->lookatY;
                    camera->lookat.z = cmd->lookatZ;
                    break;
                case MC3DiOS_CameraLookAtDelta:
                    camera->lookat.x += cmd->lookatX;
                    camera->lookat.y += cmd->lookatY;
                    camera->lookat.z += cmd->lookatZ;
                    break;
                case MC3DiOS_CameraRadius:
                    camera->R_value = cmd->radius;
                    break;
                case MC3DiOS_CameraRadiusDelta:
                    camera->R_value += cmd->radius;
                    break;
                case MC3DiOS_CameraRadiusPercent:
                    camera->R_percent = cmd->rpercent;
                    break;
                case MC3DiOS_CameraRadiusPDelta:
                    camera->R_percent += cmd->rpercent;
                    break;
                case MC3DiOS_CameraAngels:
                    camera->tht = cmd->tht;
                    camera->fai = cmd->fai;
                    if (cam2) {
                        cam2->tht   = cmd->tht;
                        cam2->fai   = cmd->fai;
                    }
                    break;
                case MC3DiOS_CameraAngelsDelta:
                    camera->tht += cmd->tht;
                    camera->fai += cmd->fai;
                    if (cam2) {
                        cam2->tht   += cmd->tht;
                        cam2->fai   += cmd->fai;
                    }
                    break;
                case MC3DiOS_GetCurrent:
                    cmd->lookatX = camera->lookat.x;
                    cmd->lookatY = camera->lookat.y;
                    cmd->lookatZ = camera->lookat.z;

                    cmd->radius = camera->R_value;
                    cmd->rpercent = camera->R_percent;

                    cmd->tht = camera->tht;
                    cmd->fai = camera->fai;

                    break;
                case MC3DiOS_LockRotation:
                    camera->isLockRotation = cmd->lockRotation;
                    break;
                default:
                    break;
            }
        }
    }
}

#define java(type, name, ...) jni(Java_com_oreisoft_beengine_BERenderer, type, name, __VA_ARGS__)

java(void, init, voida)
{
    const char* gles_version = (const char*)glGetString(GL_VERSION);
    const char* glsl_version = (const char*)glGetString(GL_SHADING_LANGUAGE_VERSION);
    debug_log("GLSL version is %s\n", glsl_version);
    if (strstr(gles_version, "OpenGL ES 3.")) {
        onTearDownGL();
        onRootViewLoad(null);
        onSetupGL(1080, 1766);
        printf("[not a error] setup OpenGL ES 3");
    } else {
        printf("Unsupported OpenGL ES version");
        exit(0);
    }
}

//java(void, loadModelBegin);
//java(void, loadModelFinish);

java(void, openFile, jstring name)
{
    const char* f = CStringFromJavaString(name);
    onOpenFile(f);
    CStringRelease(name, f);
    error_log("[not a error] JNI openFile called");
}

java(void, resize, jint width, jint height)
{
    onResizeScreen(width, height);
    error_log("[not a error] JNI resize called");
}

java(void, render, voida)
{
    onUpdate(0,0,0,0);
    onDraw();
}

java(void, setAssetManager, jobject man)
{
    MCFileSetAssetManager(AAssetManager_fromJava(env, man));
}

java(void, onGestureScale, jfloat scale)
{
    onGesturePinch(scale);
}

java(void, onGestureScroll, jdouble x, jdouble y)
{
    onGesturePan(x, y);
}


