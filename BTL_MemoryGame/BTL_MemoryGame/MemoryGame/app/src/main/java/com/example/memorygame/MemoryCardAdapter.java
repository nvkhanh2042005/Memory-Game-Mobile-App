package com.example.memorygame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemoryCardAdapter
        extends RecyclerView.Adapter<MemoryCardAdapter.CardViewHolder> {

    // =========================
    // DỮ LIỆU
    // =========================

    private final List<MemoryCard> cards;

    private final int cardHeightDp;

    private final OnCardClickListener listener;

    // =========================
    // DANH SÁCH ẢNH
    // =========================

    private static final int[] CARD_IMAGES = {

            R.drawable.card_01,
            R.drawable.card_02,
            R.drawable.card_03,
            R.drawable.card_04,
            R.drawable.card_05,
            R.drawable.card_06,
            R.drawable.card_07,
            R.drawable.card_08,
            R.drawable.card_09,
            R.drawable.card_10,
            R.drawable.card_11,
            R.drawable.card_12,
            R.drawable.card_13,
            R.drawable.card_14,
            R.drawable.card_15,
            R.drawable.card_16,
            R.drawable.card_17,
            R.drawable.card_18
    };

    // =========================
    // CLICK LISTENER
    // =========================

    public interface OnCardClickListener {

        void onCardClick(
                int position,
                MemoryCard card
        );
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    public MemoryCardAdapter(
            List<MemoryCard> cards,
            int cardHeightDp,
            OnCardClickListener listener
    ) {

        this.cards = cards;
        this.cardHeightDp = cardHeightDp;
        this.listener = listener;
    }

    // =========================
    // CREATE VIEW HOLDER
    // =========================

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_memory_card,
                                parent,
                                false
                        );

        return new CardViewHolder(view);
    }

    // =========================
    // BIND CARD
    // =========================

    @Override
    public void onBindViewHolder(
            @NonNull CardViewHolder holder,
            int position
    ) {

        MemoryCard card =
                cards.get(position);

        // =========================
        // CHIỀU CAO CARD
        // =========================

        ViewGroup.LayoutParams params =
                holder.cardContainer.getLayoutParams();

        params.height =
                dpToPx(
                        holder.itemView,
                        cardHeightDp
                );

        holder.cardContainer.setLayoutParams(
                params
        );

        // =========================
        // CARD ĐÃ GHÉP ĐÚNG
        // =========================

        if (card.isMatched()) {

            holder.itemView.setVisibility(
                    View.INVISIBLE
            );

        } else {

            holder.itemView.setVisibility(
                    View.VISIBLE
            );

            // =========================
            // CARD ĐANG MỞ
            // =========================

            if (card.isFlipped()) {

                holder.cardContainer
                        .setBackgroundResource(
                                R.drawable.card_front
                        );

                holder.tvCard.setVisibility(
                        View.GONE
                );

                holder.imgCard.setVisibility(
                        View.VISIBLE
                );

                holder.imgCard.setImageResource(
                        getCardImage(
                                card.getValue()
                        )
                );

            } else {

                // =========================
                // CARD ĐANG ÚP
                // =========================

                holder.cardContainer
                        .setBackgroundResource(
                                R.drawable.card_back
                        );

                holder.imgCard.setVisibility(
                        View.GONE
                );

                holder.tvCard.setVisibility(
                        View.VISIBLE
                );
            }
        }

        // =========================
        // CLICK CARD
        // =========================

        holder.itemView.setOnClickListener(
                view -> {

                    int currentPosition =
                            holder.getBindingAdapterPosition();

                    if (currentPosition
                            != RecyclerView.NO_POSITION) {

                        listener.onCardClick(
                                currentPosition,
                                cards.get(currentPosition)
                        );
                    }
                }
        );
    }

    // =========================
    // SỐ LƯỢNG CARD
    // =========================

    @Override
    public int getItemCount() {

        return cards.size();
    }

    // =========================
    // LẤY ẢNH CARD
    // =========================

    private int getCardImage(int value) {

        /*
         * value bắt đầu từ 1.
         * Mảng bắt đầu từ index 0.
         */

        int index =
                value - 1;

        if (index >= 0
                && index < CARD_IMAGES.length) {

            return CARD_IMAGES[index];
        }

        // Trường hợp dữ liệu không hợp lệ
        return R.drawable.card_01;
    }

    // =========================
    // DP → PX
    // =========================

    private int dpToPx(
            View view,
            int dp
    ) {

        float density =
                view.getResources()
                        .getDisplayMetrics()
                        .density;

        return Math.round(
                dp * density
        );
    }

    // =========================
    // VIEW HOLDER
    // =========================

    public static class CardViewHolder
            extends RecyclerView.ViewHolder {

        final FrameLayout cardContainer;
        final TextView tvCard;
        final ImageView imgCard;

        public CardViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            cardContainer =
                    itemView.findViewById(
                            R.id.cardContainer
                    );

            tvCard =
                    itemView.findViewById(
                            R.id.tvCard
                    );

            imgCard =
                    itemView.findViewById(
                            R.id.imgCard
                    );
        }
    }
}