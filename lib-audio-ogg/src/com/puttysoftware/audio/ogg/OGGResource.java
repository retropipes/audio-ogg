/* OGG Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-ogg
 */
package com.puttysoftware.audio.ogg;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class OGGResource extends OGGFactory {
    private final URL soundURL;
    private int number;
    private OGGPlayer player;

    public OGGResource(final ThreadGroup group, final URL resURL,
            final int taskNum) {
        super(group);
        this.soundURL = resURL;
        this.number = taskNum;
    }

    @Override
    public void run() {
        try (AudioInputStream ais = AudioSystem
                .getAudioInputStream(this.soundURL)) {
            this.player = new OGGPlayer(ais);
            this.player.playLoop();
            OGGFactory.taskCompleted(this.number);
        } catch (final UnsupportedAudioFileException e1) {
            OGGFactory.taskCompleted(this.number);
        } catch (final IOException e1) {
            OGGFactory.taskCompleted(this.number);
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
