/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.audio.ogg;

import java.lang.Thread.UncaughtExceptionHandler;

public class OggExceptionHandler implements UncaughtExceptionHandler {
    @Override
    public void uncaughtException(final Thread thr, final Throwable exc) {
        try {
            if (thr instanceof OggPlayer) {
                final OggPlayer media = (OggPlayer) thr;
                OggPlayer.taskCompleted(media.getNumber());
            }
        } catch (Throwable t) {
            // Ignore
        }
    }
}
