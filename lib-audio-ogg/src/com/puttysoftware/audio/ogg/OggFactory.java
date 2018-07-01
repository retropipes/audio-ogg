/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.audio.ogg;

import java.net.URL;

public abstract class OggFactory extends Thread {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb
    private static int ACTIVE_MEDIA_COUNT = 0;
    private static int MAX_MEDIA_ACTIVE = 5;
    private static OggFactory[] ACTIVE_MEDIA = new OggFactory[OggFactory.MAX_MEDIA_ACTIVE];
    private static ThreadGroup MEDIA_GROUP = new ThreadGroup("Ogg Media Players");
    private static OggExceptionHandler meh = new OggExceptionHandler();

    // Constructor
    protected OggFactory(final ThreadGroup group) {
        super(group, "Ogg Media Player " + OggFactory.ACTIVE_MEDIA_COUNT);
    }

    // Methods
    public abstract void stopLoop();
    
    public abstract boolean isPlaying();

    protected abstract void updateNumber(int newNumber);

    abstract int getNumber();

    // Factories
    public static OggFactory loadFile(final String file) {
        return OggFactory.provisionMedia(new OggFile(OggFactory.MEDIA_GROUP,
                file, OggFactory.ACTIVE_MEDIA_COUNT));
    }

    public static OggFactory loadResource(final URL resource) {
        return OggFactory.provisionMedia(new OggResource(OggFactory.MEDIA_GROUP,
                resource, OggFactory.ACTIVE_MEDIA_COUNT));
    }

    private static OggFactory provisionMedia(final OggFactory src) {
        if (OggFactory.ACTIVE_MEDIA_COUNT >= OggFactory.MAX_MEDIA_ACTIVE) {
            OggFactory.killAllMediaPlayers();
        }
        try {
            if (src != null) {
                src.setUncaughtExceptionHandler(OggFactory.meh);
                OggFactory.ACTIVE_MEDIA[OggFactory.ACTIVE_MEDIA_COUNT] = src;
                OggFactory.ACTIVE_MEDIA_COUNT++;
            }
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Do nothing
        }
        return src;
    }

    private static void killAllMediaPlayers() {
        OggFactory.MEDIA_GROUP.interrupt();
    }

    static synchronized void taskCompleted(final int taskNum) {
        OggFactory.ACTIVE_MEDIA[taskNum] = null;
        for (int z = taskNum + 1; z < OggFactory.ACTIVE_MEDIA.length; z++) {
            if (OggFactory.ACTIVE_MEDIA[z] != null) {
                OggFactory.ACTIVE_MEDIA[z - 1] = OggFactory.ACTIVE_MEDIA[z];
                if (OggFactory.ACTIVE_MEDIA[z - 1].isAlive()) {
                    OggFactory.ACTIVE_MEDIA[z - 1].updateNumber(z - 1);
                }
            }
        }
        OggFactory.ACTIVE_MEDIA_COUNT--;
    }
}
