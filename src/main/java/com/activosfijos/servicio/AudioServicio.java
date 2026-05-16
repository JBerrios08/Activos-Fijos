package com.activosfijos.servicio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

public final class AudioServicio {
    private AudioServicio() {
    }

    public static void reproducirClick() {
        Thread hilo = new Thread(() -> {
            try {
                InputStream recursoAudio = AudioServicio.class.getResourceAsStream("/audio/click.wav");
                if (recursoAudio == null) {
                    return;
                }
                try (InputStream flujoArchivo = recursoAudio;
                     AudioInputStream flujoAudio = AudioSystem.getAudioInputStream(flujoArchivo)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(flujoAudio);
                    clip.start();
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }, "audio-click-boton");
        hilo.setDaemon(true);
        hilo.start();
    }
}
