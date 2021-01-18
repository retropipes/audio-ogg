/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.audio.ogg;

import java.net.URL;

public abstract class OggPlayer extends Thread {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb
    private static int ACTIVE_MEDIA_COUNT = 0;
    private static int MAX_MEDIA_ACTIVE = 5;
    private static OggPlayer[] ACTIVE_MEDIA = new OggPlayer[OggPlayer.MAX_MEDIA_ACTIVE];
    private static ThreadGroup MEDIA_GROUP = new ThreadGroup(
            "Ogg Media Players");
    private static OggExceptionHandler meh = new OggExceptionHandler();

    // Constructor
    protected OggPlayer(final ThreadGroup group) {
        super(group, "Ogg Media Player " + OggPlayer.ACTIVE_MEDIA_COUNT);
    }

    // Methods
    public abstract void stopLoop();

    public abstract boolean isPlaying();

    protected abstract void updateNumber(int newNumber);

    abstract int getNumber();

    // Factories
    public static OggPlayer loadFile(final String file) {
        return OggPlayer.provisionMedia(new OggFile(OggPlayer.MEDIA_GROUP,
                file, OggPlayer.ACTIVE_MEDIA_COUNT));
    }

    public static OggPlayer loadResource(final URL resource) {
        return OggPlayer.provisionMedia(new OggResource(OggPlayer.MEDIA_GROUP,
                resource, OggPlayer.ACTIVE_MEDIA_COUNT));
    }

    public void play() {
        this.start();
    }

    private static OggPlayer provisionMedia(final OggPlayer src) {
        if (OggPlayer.ACTIVE_MEDIA_COUNT >= OggPlayer.MAX_MEDIA_ACTIVE) {
            OggPlayer.killAllMediaPlayers();
        }
        try {
            if (src != null) {
                src.setUncaughtExceptionHandler(OggPlayer.meh);
                OggPlayer.ACTIVE_MEDIA[OggPlayer.ACTIVE_MEDIA_COUNT] = src;
                OggPlayer.ACTIVE_MEDIA_COUNT++;
            }
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Do nothing
        }
        return src;
    }

    private static void killAllMediaPlayers() {
        OggPlayer.MEDIA_GROUP.interrupt();
    }

    static synchronized void taskCompleted(final int taskNum) {
        OggPlayer.ACTIVE_MEDIA[taskNum] = null;
        for (int z = taskNum + 1; z < OggPlayer.ACTIVE_MEDIA.length; z++) {
            if (OggPlayer.ACTIVE_MEDIA[z] != null) {
                OggPlayer.ACTIVE_MEDIA[z - 1] = OggPlayer.ACTIVE_MEDIA[z];
                if (OggPlayer.ACTIVE_MEDIA[z - 1].isAlive()) {
                    OggPlayer.ACTIVE_MEDIA[z - 1].updateNumber(z - 1);
                }
            }
        }
        OggPlayer.ACTIVE_MEDIA_COUNT--;
    }
}
