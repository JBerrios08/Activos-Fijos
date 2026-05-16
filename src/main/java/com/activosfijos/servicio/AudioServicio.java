package com.activosfijos.servicio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AudioServicio {
    private static final ExecutorService EJECUTOR_AUDIO = Executors.newSingleThreadExecutor(r -> {
        Thread hilo = new Thread(r, "audio-click-boton");
        hilo.setDaemon(true);
        return hilo;
    });

    private AudioServicio() {
    }

    public static void reproducirClick() {
        EJECUTOR_AUDIO.execute(() -> {
            try (InputStream recursoAudio = AudioServicio.class.getResourceAsStream("/audio/click.wav")) {
                if (recursoAudio == null) {
                    return;
                }
                try (AudioInputStream flujoAudio = AudioSystem.getAudioInputStream(recursoAudio)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(flujoAudio);
                    clip.start();
                }
            } catch (Exception ex) {
                System.err.println("No se pudo cargar audio click.wav: " + ex.getMessage());
            }
        });
    }
}
