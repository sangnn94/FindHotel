package com.sang.findhotel;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by sang on 20/12/2015.
 */
public class AnimationUtils {
  public static void recyclerViewAnimate(RecyclerView.ViewHolder holder, boolean down){
    ObjectAnimator animator = ObjectAnimator.ofFloat(holder.itemView, "TranslationY", down == true ? 200 : -200, 0);
    animator.setDuration(500);
    animator.start();
  }
}
