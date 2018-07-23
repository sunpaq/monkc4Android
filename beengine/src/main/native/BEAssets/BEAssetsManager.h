//
//  BEAssetsManager.h
//  Sapindus
//
//  Created by Sun YuLi on 16/4/30.
//  Copyright © 2016年 oreisoft. All rights reserved.
//

#ifndef BEAssetsManager_h
#define BEAssetsManager_h

#ifdef __APPLE__
#include <sys/types.h>
#endif

#ifdef __ANDROID__
#include <android/native_window.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

void MCFileSetAssetManager(AAssetManager* assetManager);
AAssetManager* MCFileGetAssetManager();
#endif

//return 0 if success
int MCFileGetPath(const char* filename, char* buffer);
int MCFileGetPathFromBundle(const char* bundlename, const char* filename, char* buffer);

const char* MCFileCopyContent(const char* filename);
const char* MCFileCopyContentWithPath(const char* filepath);
const char* MCFileCopyContentWithPathGetBufferSize(const char* filepath, off_t* buffsize);
void MCFileReleaseContent(void* buff);

#endif /* BEAssetsManager_h */
