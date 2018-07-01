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

class OggResource extends OggFactory {
    private final URL soundURL;
    private int number;
    private OggPlayer player;

    public OggResource(final ThreadGroup group, final URL resURL,
            final int taskNum) {
        super(group);
        this.soundURL = resURL;
        this.number = taskNum;
    }

    @Override
    public void run() {
        try (AudioInputStream ais = AudioSystem
                .getAudioInputStream(this.soundURL)) {
            this.player = new OggPlayer(ais);
            this.player.playLoop();
            OggFactory.taskCompleted(this.number);
        } catch (final UnsupportedAudioFileException e1) {
            OggFactory.taskCompleted(this.number);
        } catch (final IOException e1) {
            OggFactory.taskCompleted(this.number);
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
