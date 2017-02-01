/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyanogenmod.hardware;

import org.cyanogenmod.internal.util.FileUtils;

import cyanogenmod.hardware.TouchscreenGesture;

/**
 * Touchscreen gestures API
 *
 * A device may implement several touchscreen gestures for use while
 * the display is turned off, such as drawing alphabets and shapes.
 * These gestures can be interpreted by userspace to activate certain
 * actions and launch certain apps, such as to skip music tracks,
 * to turn on the flashlight, or to launch the camera app.
 *
 * This *should always* be supported by the hardware directly.
 * A lot of recent touch controllers have a firmware option for this.
 *
 * This API provides support for enumerating the gestures
 * supported by the touchscreen.
 */
public class TouchscreenGestures {

    private static final String[] GESTURE_PATHS = {
        "/proc/touchpanel/up_arrow_enable",
        "/proc/touchpanel/down_arrow_enable",
        "/proc/touchpanel/left_arrow_enable",
        "/proc/touchpanel/right_arrow_enable",
        "/proc/touchpanel/double_swipe_enable",
        "/proc/touchpanel/up_swipe_enable",
        "/proc/touchpanel/down_swipe_enable",
        "/proc/touchpanel/left_swipe_enable",
        "/proc/touchpanel/right_swipe_enable",
        "/proc/touchpanel/letter_o_enable",
    };

    // Id, name, keycode
    private static final TouchscreenGesture[] TOUCHSCREEN_GESTURES = {
        new TouchscreenGesture(0, "Up arrow", 255),
        new TouchscreenGesture(1, "Down arrow", 252),
        new TouchscreenGesture(2, "Left arrow", 253),
        new TouchscreenGesture(3, "Right arrow", 254),
        new TouchscreenGesture(4, "Two finger down swipe", 251),
        new TouchscreenGesture(5, "One finger up swipe", 66),
        new TouchscreenGesture(6, "One finger down swipe", 65),
        new TouchscreenGesture(7, "One finger left swipe", 64),
        new TouchscreenGesture(8, "One finger right swipe", 63),
        new TouchscreenGesture(9, "Letter O", 250),
    };

    /**
     * Whether device supports touchscreen gestures
     *
     * @return boolean Supported devices must return always true
     */
    public static boolean isSupported() {
        return FileUtils.isFileWritable(GESTURE_PATH) &&
                FileUtils.isFileReadable(GESTURE_PATH);
    }

    /*
     * Get the list of available gestures. A mode has an integer
     * identifier and a string name.
     *
     * It is the responsibility of the upper layers to
     * map the name to a human-readable format or perform translation.
     */
    public static TouchscreenGesture[] getAvailableGestures() {
        return TOUCHSCREEN_GESTURES;
    }

    /**
     * This method allows to set the activation status of a gesture
     *
     * @param gesture The gesture to be activated
     *        state   The new activation status of the gesture
     * @return boolean Must be false if gesture is not supported
     *         or the operation failed; true in any other case.
     */
    public static boolean setGestureEnabled(
            final TouchscreenGesture gesture, final boolean state) {
        String[] cmd = null;

        switch (gesture.id) {
            case 0:
                cmd = new String[] { "up=" };
                break;
            case 1:
                cmd = new String[] { "down=" };
                break;
            case 2:
                cmd = new String[] { "left=" };
                break;
            case 3:
                cmd = new String[] { "right=" };
                break;
            case 4:
                cmd = new String[] { "c=" };
                break;
            case 5:
                cmd = new String[] { "e=" };
                break;
            case 6:
                cmd = new String[] { "m=" };
                break;
            case 7:
                cmd = new String[] { "o=" };
                break;
            case 8:
                cmd = new String[] { "w=" };
                break;
            default:
                return false;
        }

        String enabled = state ? "true" : "false";
        StringBuilder builder = new StringBuilder();
        for (String i : cmd) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(i);
            builder.append(enabled);
        }

        return FileUtils.writeLine(GESTURE_PATH, builder.toString());
    }
}
