package com.oreisoft.beengine

import android.content.Context
import android.graphics.Rect
import android.opengl.GLSurfaceView
import android.util.Size
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by sunyuli on 2/27/2018 AD.
 */
class BERenderer(private val context: Context): GLSurfaceView.Renderer {

    interface BERendererDelegate {
        fun willDrawFrame();
        fun didDrawFrame();
    }

    enum class BECameraRotateMode {
        FixedAtOrigin,
        RotateAroundModelManual,
        RotateAroundModelByGyroscope,
        RotateAroundModelByGyroscopeReverse
    }

    private val TAG = "BERenderer"

    var delegate: BERendererDelegate? = null
    var doesAutoRotateCamera: Boolean = false
    var doesDrawWireFrame: Boolean = false
    //CMRotationMatrix deviceRotateMat3;

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        initRenderer()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        resizeAllScene(Size(width, height))
    }

    override fun onDrawFrame(gl: GL10) {
        delegate?.let {
            it.willDrawFrame()
        }
        BENativeRenderer.render()
        delegate?.let {
            it.didDrawFrame()
        }
        gl.glFlush()
    }

    fun initRenderer() {
        BENativeRenderer.initialGL()
    }

    fun cacheTextureNamed(name: String) {
        BENativeRenderer.cacheTextureNamed(name)
    }

    //+(GLKView*) createDefaultGLView:(CGRect)frame;
    //+(void) createFramebuffersWithContext:(EAGLContext*)ctx AndLayer:(CAEAGLLayer*)lyr;

    //-(instancetype) init __unavailable;
    //-(instancetype) initWithFrame:(CGRect)frame;

    //-(instancetype) setCameraRotateMode:(BECameraRotateMode)rmode;
    fun setCameraRotateMode(rmode: BECameraRotateMode) : BERenderer {

        return this
    }

    //-(instancetype) setBackgroundColor:(UIColor*)color;
    fun setBackgroundColor() : BERenderer {

        return this
    }

    //-(instancetype) resizeAllScene:(CGSize)frameSize;
    fun resizeAllScene(frameSize: Size) : BERenderer {
        BENativeRenderer.resize(frameSize.width, frameSize.height)
        return this
    }

    //-(instancetype) scissorAllScene:(CGRect)frame;
    fun scissorAllScene(frame: Rect) : BERenderer {

        return this
    }

    //-(void) removeCurrentModel;
    //-(void) addModelNamed:(NSString*)modelName;
    fun addModelNamed(modelName: String) {
        BENativeRenderer.openFile(modelName)
    }

    //-(void) addModelNamed:(NSString*)modelName Scale:(double)scale;
    fun addModelNamed(modelName: String, scale: Double) {
        BENativeRenderer.openFile(modelName)
    }

    //-(void) addModelNamed:(NSString*)modelName Scale:(double)scale RotateX:(double)ccwRadian;
    //-(void) addModelNamed:(NSString*)modelName Scale:(double)scale RotateX:(double)ccwRadian Tag:(int)tag;

    //use default if names/name is null
    //-(void) addSkyboxNamed:(NSArray*)texnames;
    //-(void) addSkysphNamed:(NSString*)texname;
    //-(void) removeCurrentSkybox;
    //-(void) removeCurrentSkysph;

    //need 4x4 column major matrix
    //-(void) cameraReset:(float*)mat4;
    //-(void) cameraReset:(float*)mat4 isRowMajor:(BOOL)rowm;

    //-(void) cameraRotate:(GLKMatrix3)mat3 Incremental:(BOOL)inc;
    //-(void) cameraTranslate:(GLKVector3)vec3 Incremental:(BOOL)inc;
    //-(void) cameraAspectRatioReset:(float)aspectRatio;
    //-(void) cameraFOVReset:(float)fov;

    //-(void) cameraTransformWorld:(GLKMatrix4)mat4;
    //-(void) cameraTransformSelf:(GLKMatrix4)mat4;

    //light pos follow camera if pos is null
    //-(void) lightReset:(GLKVector3*)pos;

    //-(void) rotateModelByPanGesture:(CGPoint)offset;
    //-(void) rotateSkysphByPanGesture:(CGPoint)offset;
    //-(void) zoomModelByPinchGesture:(CGFloat)scale;

    //-(void) updateModelTag:(int)tag PoseMat4D:(double*)mat4;
    //-(void) updateModelTag:(int)tag PoseMat4F:(float*)mat4;

    //-(void) drawFrame;
    //-(void) drawFrame:(CGRect)viewport;
    //-(void) drawFrame:(CGRect)viewport vrHeadTransform:(GLKMatrix4)head vrEyeTransform:(GLKMatrix4)eye;
    //-(void) drawFrame:(CGRect)viewport vrHeadTransform:(GLKMatrix4)head vrEyeTransform:(GLKMatrix4)eye vrFOV:(CGFloat)fov;

    //-(void) drawFrameOnCALayer:(CALayer*)calayer;
    //-(void) drawFrameOnGLView:(GLKView*)glview;

    //-(void) pauseDraw:(BOOL)pause;
}