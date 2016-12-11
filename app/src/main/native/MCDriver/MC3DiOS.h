//
//  MC3DiOS.h
//  monkcGame
//
//  Created by Sun YuLi on 16/4/6.
//  Copyright © 2016年 oreisoft. All rights reserved.
//

#ifndef MC3DiOS_h
#define MC3DiOS_h

//App lifecycle
extern void onAppStart();

//User Settings
extern int getSkyboxOn();

//GL callback
extern void onRootViewLoad(void* rootview);
extern void onReceiveMemoryWarning();
extern void onSetupGL(int windowWidth, int windowHeight);
extern void onOpenFile(const char* filename);
extern void onTearDownGL();
extern void onUpdate(double roll, double yaw, double pitch);
extern int onDraw();

//File callback
extern void onOpenExternalFile(const char* filepath);
extern void onOpenFile(const char* filename);
extern void onOpenFileAsync(const char* filename);
extern void onOpenFileAndExitThread(const char* filename);

//Gesture callback
extern void onGestureSwip();
extern void onGesturePan(double x, double y);
extern void onGesturePinch(double scale);

//Orientation Handling
extern void onResizeScreen(int windowWidth, int windowHeight);
extern void onStartStopBtn(int startOrStop);

//MCGL Errors
extern void MCGLError(const char* errmsg);
extern void MCGLStartLoading();
extern void MCGLStopLoading();

typedef enum {
    MC3DiOS_CameraLookAt,
    MC3DiOS_CameraLookAtDelta,
    MC3DiOS_CameraRadius,
    MC3DiOS_CameraRadiusDelta,
    MC3DiOS_CameraRadiusPercent,
    MC3DiOS_CameraRadiusPDelta,
    MC3DiOS_CameraAngels,
    MC3DiOS_CameraAngelsDelta,
    MC3DiOS_GetCurrent,
    MC3DiOS_LockRotation
} MC3DiOS_CameraCmdType;

typedef struct {
    MC3DiOS_CameraCmdType type;
    double lookatX;
    double lookatY;
    double lookatZ;
    double radius;
    double rpercent;
    double tht;
    double fai;
    int    lockRotation;
} MC3DiOS_CameraCmd;

extern void cameraCommand(MC3DiOS_CameraCmd* cmd);

#endif /* MC3DiOS_h */
