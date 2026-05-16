package com.activosfijos.servicio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public final class AudioServicio {
    private AudioServicio() {
    }

    public static void reproducirClick() {
        Thread hilo = new Thread(() -> {
            try {
                URL recursoAudio = AudioServicio.class.getResource("/audio/click.wav");
                if (recursoAudio == null) {
                    return;
                }
                try (AudioInputStream flujoAudio = AudioSystem.getAudioInputStream(recursoAudio)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(flujoAudio);
                    clip.addLineListener(evento -> {
                        switch (evento.getType()) {
                            case STOP -> {
                                clip.close();
                            }
                        }
                    });
                    clip.start();
                }
            } catch (Exception ignored) {
            }
        }, "audio-click-boton");
        hilo.setDaemon(true);
        hilo.start();
    }
}
