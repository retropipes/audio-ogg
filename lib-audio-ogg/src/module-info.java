/* WAV Player for Java
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/wrldwzrd89/lib-java-audio-wav
 */
/**
 *
 */
module com.puttysoftware.audio.ogg {
    exports com.puttysoftware.audio.ogg;
    uses javax.sound.sampled.spi.AudioFileReader;
    uses javax.sound.sampled.spi.FormatConversionProvider;
    requires java.desktop;
}