package com.oreisoft.beengine

import android.content.Context
import android.graphics.Point
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent

/**
 * Created by sunyuli on 2/28/2018 AD.
 */

class BEGameController {

    var renderer: BERenderer? = null

    var connected: Boolean = false

    var leftStickX: Float = 0.toFloat()
    var leftStickY: Float = 0.toFloat()
    var leftStickPressed: Boolean = false

    var rightStickX: Float = 0.toFloat()
    var rightStickY: Float = 0.toFloat()
    var rightStickPressed: Boolean = false

    var leftTriggerValue: Float = 0.toFloat()
    var leftTriggerPressed: Boolean = false
    var leftShouderPressed: Boolean = false

    var rightTriggerValue: Float = 0.toFloat()
    var rightTriggerPressed: Boolean = false
    var rightShouderPressed: Boolean = false

    companion object {
        private var instance: BEGameController? = null

        fun shared() : BEGameController? {
            if (instance == null) {
                instance = BEGameController()
                instance?.detectControllers()
            }
            return instance
        }
    }

    fun detectControllers() {
        val ids = InputDevice.getDeviceIds()
        for (id in ids) {
            val dev = InputDevice.getDevice(id)
            val sources = dev.sources
            if (sources.and(InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK) {
                connected = true
            }
        }
    }

    fun handleKeyDown(keyCode: Int, event: KeyEvent?) : Boolean {
        var handled = false;
        //val result = event?.source?.and(InputDevice.SOURCE_JOYSTICK)
        //if (result == InputDevice.SOURCE_JOYSTICK) {
            if (event?.repeatCount == 0) {
                if (keyCode == PSKeyEvent.KEYCODE_BUTTON_L2) {
                    leftTriggerPressed = true
                    renderer?.doesDrawWireFrame = true
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_BUTTON_R2) {
                    rightTriggerPressed = true
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_BUTTON_L1) {
                    leftShouderPressed = true
                } else if (keyCode == PSKeyEvent.KEYCODE_BUTTON_R1) {
                    rightShouderPressed = true
                } else if (keyCode == PSKeyEvent.KEYCODE_DPAD_UP) {
                    renderer?.doesAutoRotateCamera = true
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_DPAD_DOWN) {
                    renderer?.doesAutoRotateCamera = false
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_STICK_L) {
                    leftStickPressed = true
                } else if (keyCode == PSKeyEvent.KEYCODE_STICK_R) {
                    rightStickPressed = true
                }
            }
            if (handled) {
                return true
            }
        //}
        return true
    }

    fun handleKeyUp(keyCode: Int, event: KeyEvent?) : Boolean {
        var handled = false
        //val result = event?.source?.and(InputDevice.SOURCE_JOYSTICK)
        //if (result == InputDevice.SOURCE_JOYSTICK) {
            if (event?.repeatCount == 0) {
                if (keyCode == PSKeyEvent.KEYCODE_BUTTON_L2) {
                    leftTriggerValue = 0.toFloat()
                    leftTriggerPressed = false
                    renderer?.doesDrawWireFrame = false
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_BUTTON_R2) {
                    rightTriggerValue = 0.toFloat()
                    rightTriggerPressed = false
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_BUTTON_L1) {
                    leftShouderPressed = false
                } else if (keyCode == PSKeyEvent.KEYCODE_BUTTON_R1) {
                    rightShouderPressed = false
                } else if (keyCode == PSKeyEvent.KEYCODE_DPAD_UP) {
                    renderer?.doesAutoRotateCamera = true
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_DPAD_DOWN) {
                    renderer?.doesAutoRotateCamera = false
                    handled = true;
                } else if (keyCode == PSKeyEvent.KEYCODE_STICK_L) {
                    leftStickX = 0.toFloat()
                    leftStickY = 0.toFloat()
                    leftStickPressed = false
                } else if (keyCode == PSKeyEvent.KEYCODE_STICK_R) {
                    rightStickX = 0.toFloat()
                    rightStickY = 0.toFloat()
                    rightStickPressed = false
                }
            }
            if (handled) {
                return true
            }
        //}
        return true
    }

    //MotionEvent.AXIS_X
    fun handleGenericMotionEvent(event: MotionEvent?) : Boolean {
        event?.getAxisValue(MotionEvent.AXIS_X)?.let {
            leftStickX = it
        }
        event?.getAxisValue(MotionEvent.AXIS_Y)?.let {
            rightStickY = it
        }
        event?.getAxisValue(MotionEvent.AXIS_LTRIGGER)?.let {
            leftTriggerValue = it
        }
        event?.getAxisValue(MotionEvent.AXIS_RX)?.let {
            leftTriggerValue = it
        }
        event?.getAxisValue(MotionEvent.AXIS_RY)?.let {
            rightTriggerValue = it
        }

        return true
    }
}