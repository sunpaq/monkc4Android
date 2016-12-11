cmake_minimum_required(VERSION 3.4.1)

enable_language(ASM C)
set(MCTrampolineSrc src/main/native/MonkC/MCTrampoline.S)
set_property(SOURCE ${MCTrampolineSrc} PROPERTY LANGUAGE C)

set(MonkCDir           src/main/native/MonkC)
set(LemonteaDir        src/main/native/Lemontea)
set(BEAssetsManagerDir src/main/native/BEAssetsManager)
set(BEEngineDir        src/main/native/BEEngine)
set(SOILDir            src/main/native/BEAssetsManager/external/SOIL)
set(MCDriverDir        src/main/native/MCDriver)
set(AppDir             src/main/native/App)

# Monk-C
aux_source_directory(${MonkCDir}     MonkCSrc)
aux_source_directory(${LemonteaDir}  LemonteaSrc)
# BohdiEngine
aux_source_directory(${BEAssetsManagerDir} BEAssetsManagerSrc)
aux_source_directory(${BEEngineDir}        BEEngineSrc)
aux_source_directory(${SOILDir}            SOILSrc)
# App
aux_source_directory(${MCDriverDir}        MCDriverSrc)
aux_source_directory(${AppDir}             AppSrc)

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ${MCTrampolineSrc}
             ${MonkCSrc}
             ${LemonteaSrc}
             ${BEAssetsManagerSrc}
             ${BEEngineSrc}
             ${SOILSrc}
             ${MCDriverSrc}
             ${AppSrc})

include_directories(
             ${MonkCDir}
             ${LemonteaDir}
             ${BEAssetsManagerDir}
             ${BEEngineDir}
             ${SOILDir}
             ${MCDriverDir}
             ${AppDir})

find_library(log-lib log)
find_library(egl-lib EGL)
find_library(gles-lib GLESv3)
find_library(android-lib android)

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       ${log-lib}
                       ${egl-lib}
                       ${gles-lib}
                       ${android-lib})


