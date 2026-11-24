package com.example.memorygame;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {

    private static MediaPlayer mediaPlayer;

    private MusicManager() {
    }

    public static void play(Context context) {

        // Luôn dọn player cũ trước
        stop();

        mediaPlayer =
                MediaPlayer.create(
                        context.getApplicationContext(),
                        R.raw.background_music
                );

        if (mediaPlayer != null) {

            mediaPlayer.setLooping(true);

            mediaPlayer.start();
        }
    }

    public static void stop() {

        if (mediaPlayer != null) {

            try {
                mediaPlayer.stop();
            } catch (IllegalStateException ignored) {
            }

            mediaPlayer.release();

            mediaPlayer = null;
        }
    }
}