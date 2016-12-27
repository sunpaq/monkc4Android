//
//  MC3DAndroidDriver.c
//  Sapindus
//
//  Created by Sun YuLi on 16/7/9.
//  Copyright © 2016年 oreisoft. All rights reserved.
//

#ifdef __ANDROID__

#include "monkc.h"
#include <stdio.h>
#include "MC3DiOS.h"
#include "MCJNI.h"

//MCGL Errors
void MCGLError(const char* errmsg)
{
	debug_log("MCGLError: %s\n", errmsg);
}

int getSkyboxOn()
{
    return 0;
}

//call Java

void MCGLStartLoading()
{
    callJavaInt(BEEngineView, MCGLStartLoading, 0);
}

void MCGLStopLoading()
{
    callJavaInt(BEEngineView, MCGLStopLoading, 0);
}

#endif

