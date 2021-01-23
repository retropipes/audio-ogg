/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.audio.ogg;

import java.net.URL;

public abstract class OggPlayer extends Thread {
    // Constants
    private static OggPlayer ACTIVE_MEDIA;

    // Constructor
    protected OggPlayer() {
        super("Ogg Media Player");
    }

    // Methods
    protected abstract void stopPlayer();

    public abstract boolean isPlaying();

    // Factories
    public static OggPlayer loadLoopedFile(final String file) {
        OggPlayer.stopPlaying();
        OggPlayer.ACTIVE_MEDIA = new OggLoopFile(file);
        return OggPlayer.ACTIVE_MEDIA;
    }

    public static OggPlayer loadLoopedResource(final URL resource) {
        OggPlayer.stopPlaying();
        OggPlayer.ACTIVE_MEDIA = new OggLoopResource(resource);
        return OggPlayer.ACTIVE_MEDIA;
    }

    public static OggPlayer loadOneShotFile(final String file) {
        OggPlayer.stopPlaying();
        OggPlayer.ACTIVE_MEDIA = new OggFile(file);
        return OggPlayer.ACTIVE_MEDIA;
    }

    public static OggPlayer loadOneShotResource(final URL resource) {
        OggPlayer.stopPlaying();
        OggPlayer.ACTIVE_MEDIA = new OggResource(resource);
        return OggPlayer.ACTIVE_MEDIA;
    }

    public void play() {
        this.start();
    }

    public static void stopPlaying() {
        if (OggPlayer.ACTIVE_MEDIA != null) {
            OggPlayer.ACTIVE_MEDIA.stopPlayer();
        }
    }
}
