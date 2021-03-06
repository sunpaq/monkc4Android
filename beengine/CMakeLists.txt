cmake_minimum_required(VERSION 3.4.1)

enable_language(ASM C)

set(SOILDir            src/main/native/External/SOIL)
set(MonkCDir           src/main/native/Classes/MonkC)
set(BEAssetsDir        src/main/native/Classes/BEAssets)
set(BackendDir         src/main/native/Classes/Backend/OpenGL)
set(BEEngineDir        src/main/native/Classes/Engine)
set(InterfaceDir       src/main/native/Classes/Interface)
set(AppDir             src/main/native/App)

# Monk-C
aux_source_directory(${MonkCDir}     MonkCSrc)
# BohdiEngine
aux_source_directory(${BEAssetsDir}  BEAssetsSrc)
aux_source_directory(${BEEngineDir}  BEEngineSrc)
aux_source_directory(${BackendDir}   BackendSrc)
aux_source_directory(${SOILDir}      SOILSrc)
# App
aux_source_directory(${InterfaceDir} InterfaceSrc)
aux_source_directory(${AppDir}       AppSrc)

add_library( # Sets the name of the library.
             native_renderer

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ${MonkCSrc}
             ${BEAssetsSrc}
             ${BackendSrc}
             ${BEEngineSrc}

             ${SOILSrc}
             ${InterfaceSrc}
             ${AppSrc})

include_directories(
             ${MonkCDir}
             ${BEAssetsDir}
             ${BEEngineDir}
             ${BackendDir}
             ${SOILDir}
             ${InterfaceDir}
             ${AppDir})

find_library(log-lib log)
find_library(egl-lib EGL)
find_library(gles-lib GLESv3)
find_library(android-lib android)

target_link_libraries( # Specifies the target library.
                       native_renderer

                       # Links the target library to the log library
                       ${log-lib}
                       ${egl-lib}
                       ${gles-lib}
                       ${android-lib})


