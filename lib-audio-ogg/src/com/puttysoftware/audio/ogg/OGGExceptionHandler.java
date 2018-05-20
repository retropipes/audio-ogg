/* OGG Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-ogg
 */
package com.puttysoftware.audio.ogg;

import java.lang.Thread.UncaughtExceptionHandler;

public class OGGExceptionHandler implements UncaughtExceptionHandler {
    @Override
    public void uncaughtException(final Thread thr, final Throwable exc) {
        try {
            if (thr instanceof OGGFactory) {
                final OGGFactory media = (OGGFactory) thr;
                OGGFactory.taskCompleted(media.getNumber());
            }
        } catch (Throwable t) {
            // Ignore
        }
    }
}
