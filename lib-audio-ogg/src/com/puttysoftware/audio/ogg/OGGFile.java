/* OGG Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-ogg
 */
package com.puttysoftware.audio.ogg;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OGGFile extends OGGFactory {
    private final String filename;
    private int number;
    private OGGPlayer player;

    public OGGFile(final ThreadGroup group, final String oggfile,
            final int taskNum) {
        super(group);
        this.filename = oggfile;
        this.number = taskNum;
    }

    @Override
    public void run() {
        if (this.filename != null) {
            final File soundFile = new File(this.filename);
            if (!soundFile.exists()) {
                OGGFactory.taskCompleted(this.number);
                return;
            }
            try (AudioInputStream ais = AudioSystem
                    .getAudioInputStream(soundFile)) {
                this.player = new OGGPlayer(ais);
                this.player.playLoop();
                OGGFactory.taskCompleted(this.number);
            } catch (final UnsupportedAudioFileException e1) {
                OGGFactory.taskCompleted(this.number);
            } catch (final IOException e1) {
                OGGFactory.taskCompleted(this.number);
            }
        }
    }

    @Override
    public void stopLoop() {
        if (this.player != null) {
            this.player.stopLoop();
        }
    }

    @Override
    int getNumber() {
        return this.number;
    }

    @Override
    protected void updateNumber(final int newNumber) {
        this.number = newNumber;
    }
}
