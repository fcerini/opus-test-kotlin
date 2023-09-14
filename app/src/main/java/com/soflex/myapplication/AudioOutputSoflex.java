package com.soflex.myapplication;

import android.util.Log;

public class AudioOutputSoflex {

    private static int PACKAGES = 3840;
//    private static int PACKAGES = 5;


    // Cantidad de paquetes que tiene que tener el audio para empezar la ejecucion
    private static int AUDIO_START_PACKAGES = PACKAGES * 15;
    // Cantidad de tiempo en milisegundos que tiene que pasar para que se concidere como un nuevo paquete
    private static int LAST_PACKET_TIME = 1000;

    private static int SEQUENCE = 1;

    private int ultimoIndiceProcesado;


    private byte[] audio = new byte[0];

    private long lastSeq = 0;

    private final long timestamp;

    public AudioOutputSoflex() {
        ultimoIndiceProcesado = 0;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isLast() {
        return (System.currentTimeMillis() - timestamp) > LAST_PACKET_TIME ;
    }


    public void setPacketAudio(byte[] audioOpusPacket, int seq) {

        if(lastSeq < seq) {
            long missingPackets = 0;
            if(lastSeq != 0) {
                missingPackets = (seq - lastSeq) / SEQUENCE - 1;
                if(missingPackets > 0) {
                    byte[] emptyAudio = new byte[(int) (missingPackets * PACKAGES)];
                    this.setAudioPacket(emptyAudio);
                }
            }

            lastSeq = seq;

            if(audioOpusPacket != null) {
                this.setAudioPacket(audioOpusPacket);
            }
        } else {

            try {
                long missingPackets = (lastSeq - seq) / SEQUENCE;
                setLostAudioPacket(audioOpusPacket, (int) missingPackets);
            } catch (Exception e) {

            }
        }
    }

    public byte[] clipPCM(byte[] signal, byte threshold) {
        byte[] clippedSignal = new byte[signal.length];

        for (int i = 0; i < signal.length; i++) {
            // Aplicar el clipping a los valores de la señal
            clippedSignal[i] = (byte) Math.max(-threshold, Math.min(threshold, signal[i]));
        }

        return clippedSignal;
    }

    private void setAudioPacket(byte[] audioPacket) {
        if(audioPacket != null) {
            byte[] newAudio = new byte[audio.length + audioPacket.length];
            System.arraycopy(audio, 0, newAudio, 0, audio.length);
            System.arraycopy(audioPacket, 0, newAudio, audio.length, audioPacket.length);
            audio = newAudio;
        }
    }

    private void setLostAudioPacket(byte[] audioPacket , int missingPackets) {
        if(audioPacket != null) {
            byte[] newAudio;
            if(audioPacket.length != audio.length) {
                newAudio = new byte[audio.length];
                System.arraycopy(audio, 0, newAudio, 0, audio.length);
                System.arraycopy(audioPacket, 0, newAudio, audio.length - (PACKAGES * missingPackets) - PACKAGES, audioPacket.length);
            } else {
                newAudio = new byte[audio.length * 2];
                System.arraycopy(audioPacket, 0, newAudio, 0, audioPacket.length);
                System.arraycopy(audio, 0, newAudio, audioPacket.length, audio.length);
            }
            audio = newAudio;

        }
    }

    public byte[] getPacketAudio() {
        int longitudNovedades = audio.length - ultimoIndiceProcesado;
        byte[] novedades = new byte[longitudNovedades];

        System.arraycopy(audio, ultimoIndiceProcesado, novedades, 0, longitudNovedades);
        ultimoIndiceProcesado = audio.length;

        return novedades;
    }


    public boolean isAudioFinished() {
        return ultimoIndiceProcesado == audio.length;
    }

    public boolean isReadyToListen() {
        return audio.length >= AUDIO_START_PACKAGES;
    }

    public byte[] getAudio() {
        return audio;
    }

}