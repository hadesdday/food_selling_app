package com.example.food_selling_app;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    public interface ClickListener {
        void onTouch(View view, int position);
        void onHold(View view, int position);
    }

    private final ClickListener listener;
    private final GestureDetector detector;

    public RecyclerTouchListener(Context context, RecyclerView recyclerView, ClickListener listener) {
        this.listener = listener;
        this.detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && listener != null)
                    listener.onHold(child, recyclerView.getChildAdapterPosition(child));
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && listener != null && detector.onTouchEvent(e))
            listener.onTouch(child, rv.getChildAdapterPosition(child));

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}
