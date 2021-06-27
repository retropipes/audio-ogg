/* Ogg Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-Ogg
 */
package com.puttysoftware.audio.ogg;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OggFile extends OggPlayer {
    private final String filename;
    private OggPlayThread player;

    public OggFile(final String Oggfile) {
        super();
        this.filename = Oggfile;
    }

    @Override
    public void run() {
        if (this.filename != null) {
            final File soundFile = new File(this.filename);
            if (!soundFile.exists()) {
                return;
            }
            try (AudioInputStream ais = AudioSystem
                    .getAudioInputStream(soundFile)) {
                this.player = new OggPlayThread(ais);
                this.player.play();
            } catch (final UnsupportedAudioFileException e1) {
            } catch (final IOException e1) {
            }
        }
    }

    @Override
    public boolean isPlaying() {
        return this.player != null && this.isAlive();
    }

    @Override
    public void stopPlayer() {
        if (this.player != null) {
            this.player.stopPlaying();
        }
    }
}
