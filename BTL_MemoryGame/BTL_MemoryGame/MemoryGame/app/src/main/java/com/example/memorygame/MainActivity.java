package com.example.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // =========================
    // GIAO DIỆN
    // =========================

    private RecyclerView recyclerViewGame;
    private MemoryCardAdapter adapter;

    private TextView tvScore;
    private TextView tvTries;

    // =========================
    // DỮ LIỆU GAME
    // =========================

    private List<MemoryCard> cards;

    private int firstCardPosition = -1;
    private int secondCardPosition = -1;

    private int score = 0;
    private int tries = 0;

    private boolean isCheckingPair = false;

    // =========================
    // KÍCH THƯỚC BÀN
    // =========================

    private int boardColumns = 4;
    private int boardRows = 4;

    // =========================
    // SETTINGS
    // =========================

    private static final String PREFS_NAME =
            "game_settings";

    private static final String KEY_MUSIC_ENABLED =
            "music_enabled";

    // =========================
    // HANDLER
    // =========================

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    // =========================
    // ON CREATE
    // =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerViewGame =
                findViewById(R.id.recyclerViewGame);

        tvScore =
                findViewById(R.id.tvScore);

        tvTries =
                findViewById(R.id.tvTries);

        Button btnNewGame =
                findViewById(R.id.btnNewGame);

        ImageButton btnSettings =
                findViewById(R.id.btnSettings);

        Spinner spinnerBoardSize =
                findViewById(R.id.spinnerBoardSize);

        // =========================
        // CHỌN KÍCH THƯỚC BÀN
        // =========================

        spinnerBoardSize.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id
                    ) {

                        switch (position) {

                            case 0:
                                boardColumns = 4;
                                boardRows = 4;
                                break;

                            case 1:
                                boardColumns = 4;
                                boardRows = 5;
                                break;

                            case 2:
                                boardColumns = 4;
                                boardRows = 6;
                                break;

                            case 3:
                                boardColumns = 5;
                                boardRows = 6;
                                break;

                            case 4:
                                boardColumns = 6;
                                boardRows = 6;
                                break;
                        }

                        startNewGame();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                    }
                }
        );

        // =========================
        // NEW GAME
        // =========================

        btnNewGame.setOnClickListener(
                view -> startNewGame()
        );

        // =========================
        // SETTINGS
        // =========================

        btnSettings.setOnClickListener(view -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });
    }

    // =========================
    // BẮT ĐẦU VÁN MỚI
    // =========================

    private void startNewGame() {

        handler.removeCallbacksAndMessages(null);

        score = 0;
        tries = 0;

        tvScore.setText(
                String.valueOf(score)
        );

        tvTries.setText(
                String.valueOf(tries)
        );

        firstCardPosition = -1;
        secondCardPosition = -1;

        isCheckingPair = false;

        // =========================
        // GRID
        // =========================

        GridLayoutManager layoutManager =
                new GridLayoutManager(
                        this,
                        boardColumns
                );

        recyclerViewGame.setLayoutManager(
                layoutManager
        );

        // =========================
        // KÍCH THƯỚC CARD
        // =========================

        int cardHeightDp =
                getCardHeightDp();

        int rowHeightDp =
                cardHeightDp + 8;

        int boardHeightDp =
                boardRows * rowHeightDp;

        ViewGroup.LayoutParams params =
                recyclerViewGame.getLayoutParams();

        params.height =
                dpToPx(boardHeightDp);

        recyclerViewGame.setLayoutParams(
                params
        );

        // =========================
        // TẠO CARD
        // =========================

        cards =
                createCards();

        adapter =
                new MemoryCardAdapter(
                        cards,
                        cardHeightDp,
                        this::handleCardClick
                );

        recyclerViewGame.setAdapter(
                adapter
        );
    }

    // =========================
    // CHIỀU CAO CARD
    // =========================

    private int getCardHeightDp() {

        if (boardRows == 4) {

            // 4 × 4
            return 72;

        } else if (boardRows == 5) {

            // 4 × 5
            return 64;

        } else if (boardColumns == 4) {

            // 4 × 6
            return 58;

        } else if (boardColumns == 5) {

            // 5 × 6
            return 56;

        } else {

            // 6 × 6
            return 52;
        }
    }

    // =========================
    // CLICK CARD
    // =========================

    private void handleCardClick(
            int position,
            MemoryCard card
    ) {

        if (isCheckingPair) {
            return;
        }

        if (card.isFlipped()
                || card.isMatched()) {

            return;
        }

        card.setFlipped(true);

        adapter.notifyItemChanged(
                position
        );

        // Card đầu tiên
        if (firstCardPosition == -1) {

            firstCardPosition =
                    position;

        } else {

            // Card thứ hai
            secondCardPosition =
                    position;

            tries++;

            tvTries.setText(
                    String.valueOf(tries)
            );

            checkPair();
        }
    }

    // =========================
    // KIỂM TRA CẶP
    // =========================

    private void checkPair() {

        isCheckingPair = true;

        int firstPosition =
                firstCardPosition;

        int secondPosition =
                secondCardPosition;

        MemoryCard firstCard =
                cards.get(firstPosition);

        MemoryCard secondCard =
                cards.get(secondPosition);

        // =========================
        // GIỐNG NHAU
        // =========================

        if (firstCard.getValue()
                == secondCard.getValue()) {

            handler.postDelayed(() -> {

                firstCard.setMatched(true);
                secondCard.setMatched(true);

                score++;

                tvScore.setText(
                        String.valueOf(score)
                );

                adapter.notifyItemChanged(
                        firstPosition
                );

                adapter.notifyItemChanged(
                        secondPosition
                );

                resetSelectedCards();

                if (score
                        == cards.size() / 2) {

                    showWinDialog();
                }

            }, 700);

        } else {

            // =========================
            // KHÁC NHAU
            // =========================

            handler.postDelayed(() -> {

                firstCard.setFlipped(false);
                secondCard.setFlipped(false);

                adapter.notifyItemChanged(
                        firstPosition
                );

                adapter.notifyItemChanged(
                        secondPosition
                );

                resetSelectedCards();

            }, 700);
        }
    }

    // =========================
    // RESET CARD ĐANG CHỌN
    // =========================

    private void resetSelectedCards() {

        firstCardPosition = -1;
        secondCardPosition = -1;

        isCheckingPair = false;
    }

    // =========================
    // HỘP THOẠI THẮNG
    // =========================

    private void showWinDialog() {

        // Dừng nhạc khi thắng
        MusicManager.stop();

        AlertDialog dialog =
                new AlertDialog.Builder(this)

                        .setTitle(
                                getString(
                                        R.string.win_title
                                )
                        )

                        .setMessage(
                                getString(
                                        R.string.win_message,
                                        score,
                                        tries
                                )
                        )

                        .setCancelable(false)

                        .setPositiveButton(
                                getString(
                                        R.string.play_again
                                ),
                                (dialogInterface, which) ->
                                        startNewGame()
                        )

                        .create();

        // Đóng dialog → bật lại nhạc nếu Settings đang ON
        dialog.setOnDismissListener(
                dialogInterface ->
                        applyMusicSetting()
        );

        dialog.show();
    }

    // =========================
    // TẠO DANH SÁCH CARD
    // =========================

    private List<MemoryCard> createCards() {

        List<MemoryCard> newCards =
                new ArrayList<>();

        int totalCards =
                boardColumns * boardRows;

        int pairCount =
                totalCards / 2;

        for (int value = 1;
             value <= pairCount;
             value++) {

            newCards.add(
                    new MemoryCard(value)
            );

            newCards.add(
                    new MemoryCard(value)
            );
        }

        Collections.shuffle(
                newCards
        );

        return newCards;
    }

    // =========================
    // ĐỌC SETTING NHẠC
    // =========================

    private boolean isMusicEnabled() {

        SharedPreferences preferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        return preferences.getBoolean(
                KEY_MUSIC_ENABLED,
                true
        );
    }

    // =========================
    // ÁP DỤNG SETTING NHẠC
    // =========================

    private void applyMusicSetting() {

        if (isMusicEnabled()) {

            MusicManager.play(this);

        } else {

            MusicManager.stop();
        }
    }

    // =========================
    // DP → PX
    // =========================

    private int dpToPx(int dp) {

        float density =
                getResources()
                        .getDisplayMetrics()
                        .density;

        return Math.round(
                dp * density
        );
    }

    // =========================
    // LIFECYCLE
    // =========================

    @Override
    protected void onResume() {
        super.onResume();

        applyMusicSetting();
    }

    @Override
    protected void onStop() {
        super.onStop();

        MusicManager.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacksAndMessages(null);

        MusicManager.stop();
    }
}