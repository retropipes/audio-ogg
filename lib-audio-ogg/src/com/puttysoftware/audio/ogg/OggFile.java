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

class OggFile extends OggFactory {
    private final String filename;
    private int number;
    private OggPlayer player;

    public OggFile(final ThreadGroup group, final String Oggfile,
            final int taskNum) {
        super(group);
        this.filename = Oggfile;
        this.number = taskNum;
    }

    @Override
    public void run() {
        if (this.filename != null) {
            final File soundFile = new File(this.filename);
            if (!soundFile.exists()) {
                OggFactory.taskCompleted(this.number);
                return;
            }
            try (AudioInputStream ais = AudioSystem
                    .getAudioInputStream(soundFile)) {
                this.player = new OggPlayer(ais);
                this.player.playLoop();
                OggFactory.taskCompleted(this.number);
            } catch (final UnsupportedAudioFileException e1) {
                OggFactory.taskCompleted(this.number);
            } catch (final IOException e1) {
                OggFactory.taskCompleted(this.number);
            }
        }
    }
    
    @Override
    public boolean isPlaying() {
    	return this.player != null && this.isAlive();
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
