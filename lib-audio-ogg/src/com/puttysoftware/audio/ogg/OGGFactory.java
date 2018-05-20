/* OGG Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-ogg
 */
package com.puttysoftware.audio.ogg;

import java.net.URL;

public abstract class OGGFactory extends Thread {
    // Constants
    protected static final int EXTERNAL_BUFFER_SIZE = 4096; // 4Kb
    private static int ACTIVE_MEDIA_COUNT = 0;
    private static int MAX_MEDIA_ACTIVE = 5;
    private static OGGFactory[] ACTIVE_MEDIA = new OGGFactory[OGGFactory.MAX_MEDIA_ACTIVE];
    private static ThreadGroup MEDIA_GROUP = new ThreadGroup("OGG Media Players");
    private static OGGExceptionHandler meh = new OGGExceptionHandler();

    // Constructor
    protected OGGFactory(final ThreadGroup group) {
        super(group, "OGG Media Player " + OGGFactory.ACTIVE_MEDIA_COUNT);
    }

    // Methods
    public abstract void stopLoop();

    protected abstract void updateNumber(int newNumber);

    abstract int getNumber();

    // Factories
    public static OGGFactory loadFile(final String file) {
        return OGGFactory.provisionMedia(new OGGFile(OGGFactory.MEDIA_GROUP,
                file, OGGFactory.ACTIVE_MEDIA_COUNT));
    }

    public static OGGFactory loadResource(final URL resource) {
        return OGGFactory.provisionMedia(new OGGResource(OGGFactory.MEDIA_GROUP,
                resource, OGGFactory.ACTIVE_MEDIA_COUNT));
    }

    private static OGGFactory provisionMedia(final OGGFactory src) {
        if (OGGFactory.ACTIVE_MEDIA_COUNT >= OGGFactory.MAX_MEDIA_ACTIVE) {
            OGGFactory.killAllMediaPlayers();
        }
        try {
            if (src != null) {
                src.setUncaughtExceptionHandler(OGGFactory.meh);
                OGGFactory.ACTIVE_MEDIA[OGGFactory.ACTIVE_MEDIA_COUNT] = src;
                OGGFactory.ACTIVE_MEDIA_COUNT++;
            }
        } catch (final ArrayIndexOutOfBoundsException aioob) {
            // Do nothing
        }
        return src;
    }

    private static void killAllMediaPlayers() {
        OGGFactory.MEDIA_GROUP.interrupt();
    }

    static synchronized void taskCompleted(final int taskNum) {
        OGGFactory.ACTIVE_MEDIA[taskNum] = null;
        for (int z = taskNum + 1; z < OGGFactory.ACTIVE_MEDIA.length; z++) {
            if (OGGFactory.ACTIVE_MEDIA[z] != null) {
                OGGFactory.ACTIVE_MEDIA[z - 1] = OGGFactory.ACTIVE_MEDIA[z];
                if (OGGFactory.ACTIVE_MEDIA[z - 1].isAlive()) {
                    OGGFactory.ACTIVE_MEDIA[z - 1].updateNumber(z - 1);
                }
            }
        }
        OGGFactory.ACTIVE_MEDIA_COUNT--;
    }
}
