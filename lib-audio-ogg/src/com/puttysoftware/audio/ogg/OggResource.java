/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.audio.ogg;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OggResource extends OggPlayer {
    private final URL soundURL;
    private OggPlayThread player;

    public OggResource(final URL resURL) {
        super();
        this.soundURL = resURL;
    }

    @Override
    public void run() {
        try (AudioInputStream ais = AudioSystem
                .getAudioInputStream(this.soundURL)) {
            this.player = new OggPlayThread(ais);
            this.player.play();
        } catch (final UnsupportedAudioFileException e1) {
        } catch (final IOException e1) {
        }
    }

    @Override
    public boolean isPlaying() {
        return this.player != null && this.isAlive();
    }

    @Override
    protected void stopPlayer() {
        if (this.player != null) {
            this.player.stopPlaying();
        }
    }
}
