package com.test.admin.conurbations.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.test.admin.conurbations.BuildConfig;

/**
 * Created by zhouqiong on 18/04/2016. (dd/MM/yyyy).
 * <p>
 * Utility class used to easily animate Views. Most used for revealing or hiding Views.
 */
public class AnimationUtils {

    public static final int ANIMATION_DURATION_SHORTEST = 150;
    public static final int ANIMATION_DURATION_SHORT = 250;

    @TargetApi(21)
    public static void circleRevealView(View view, int duration) {
        // get the center for the clipping circle
        int cx = view.getWidth();
        int cy = view.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        if (duration > 0) {
            anim.setDuration(duration);
        } else {
            anim.setDuration(ANIMATION_DURATION_SHORT);
        }

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(21)
    public static void circleRevealView(View view) {
        circleRevealView(view, ANIMATION_DURATION_SHORT);
    }

    @TargetApi(21)
    public static void circleHideView(final View view, AnimatorListenerAdapter listenerAdapter) {
        circleHideView(view, ANIMATION_DURATION_SHORT, listenerAdapter);
    }

    @TargetApi(21)
    public static void circleHideView(final View view, int duration, AnimatorListenerAdapter listenerAdapter) {
        // get the center for the clipping circle
        int cx = view.getWidth();
        int cy = view.getHeight() / 2;

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(listenerAdapter);

        if (duration > 0) {
            anim.setDuration(duration);
        } else {
            anim.setDuration(ANIMATION_DURATION_SHORT);
        }
        // start the animation
        anim.start();
    }

    public static void fadeInView(View view) {
        fadeInView(view, ANIMATION_DURATION_SHORTEST);
    }

    /**
     * Reveal the provided View with a fade-in animation.
     *
     * @param view     The View that's being animated.
     * @param duration How long should the animation take, in millis.
     */
    public static void fadeInView(View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);

        // Setting the listener to null, so it won't keep getting called.
        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(null);
    }

    /**
     * Hide the provided View with a fade-out animation. Fast.
     *
     * @param view The View that's being animated.
     */
    public static void fadeOutView(View view) {
        fadeOutView(view, ANIMATION_DURATION_SHORTEST);
    }

    /**
     * Hide the provided View with a fade-out animation.
     *
     * @param view     The View that's being animated.
     * @param duration How long should the animation take, in millis.
     */
    public static void fadeOutView(final View view, int duration) {
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                view.setDrawingCacheEnabled(true);
            }

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.GONE);
                view.setAlpha(1f);
                view.setDrawingCacheEnabled(false);
            }

            @Override
            public void onAnimationCancel(View view) {
            }
        });
    }

    /**
     * Cross animate two views, showing one, hiding the other.
     *
     * @param showView The View that's going to be visible after the animation.
     * @param hideView The View that's going to disappear after the animation.
     */
    public static void crossFadeViews(View showView, View hideView) {
        crossFadeViews(showView, hideView, ANIMATION_DURATION_SHORT);
    }

    public static void crossFadeViews(View showView, final View hideView, int duration) {
        fadeInView(showView, duration);
        fadeOutView(hideView, duration);
    }

    // TODO - Cross fade with circle reveal.


    private static final String TAG = "AnimationUtils";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public enum Type {
        ALPHA,
        SCALE_AND_ALPHA, LIGHT_SCALE_AND_ALPHA,
        SLIDE_AND_ALPHA, LIGHT_SLIDE_AND_ALPHA
    }

    public static void animateView(View view, boolean enterOrExit, long duration) {
        animateView(view, Type.ALPHA, enterOrExit, duration, 0, null);
    }

    public static void animateView(View view, boolean enterOrExit, long duration, long delay) {
        animateView(view, Type.ALPHA, enterOrExit, duration, delay, null);
    }

    public static void animateView(View view, boolean enterOrExit, long duration, long delay, Runnable execOnEnd) {
        animateView(view, Type.ALPHA, enterOrExit, duration, delay, execOnEnd);
    }

    public static void animateView(View view, Type animationType, boolean enterOrExit, long duration) {
        animateView(view, animationType, enterOrExit, duration, 0, null);
    }

    public static void animateView(View view, Type animationType, boolean enterOrExit, long duration, long delay) {
        animateView(view, animationType, enterOrExit, duration, delay, null);
    }

    /**
     * Animate the view
     *
     * @param view          view that will be animated
     * @param animationType {@link Type} of the animation
     * @param enterOrExit   true to enter, false to exit
     * @param duration      how long the animation will take, in milliseconds
     * @param delay         how long the animation will wait to start, in milliseconds
     * @param execOnEnd     runnable that will be executed when the animation ends
     */
    public static void animateView(final View view, Type animationType, boolean enterOrExit, long duration, long delay, Runnable execOnEnd) {
        if (DEBUG) {
            String id;
            try {
                id = view.getResources().getResourceEntryName(view.getId());
            } catch (Exception e) {
                id = view.getId() + "";
            }

            String msg = String.format("%8s →  [%s:%s] [%s %s:%s] execOnEnd=%s",
                    enterOrExit, view.getClass().getSimpleName(), id, animationType, duration, delay, execOnEnd);
            Log.d(TAG, "animateView()" + msg);
        }

        if (view.getVisibility() == View.VISIBLE && enterOrExit) {
            if (DEBUG) Log.d(TAG, "animateView() view was already visible > view = [" + view + "]");
            view.animate().setListener(null).cancel();
            view.setVisibility(View.VISIBLE);
            view.setAlpha(1f);
            if (execOnEnd != null) execOnEnd.run();
            return;
        } else if ((view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) && !enterOrExit) {
            if (DEBUG) Log.d(TAG, "animateView() view was already gone > view = [" + view + "]");
            view.animate().setListener(null).cancel();
            view.setVisibility(View.GONE);
            view.setAlpha(0f);
            if (execOnEnd != null) execOnEnd.run();
            return;
        }

        view.animate().setListener(null).cancel();
        view.setVisibility(View.VISIBLE);

        switch (animationType) {
            case ALPHA:
                animateAlpha(view, enterOrExit, duration, delay, execOnEnd);
                break;
            case SCALE_AND_ALPHA:
                animateScaleAndAlpha(view, enterOrExit, duration, delay, execOnEnd);
                break;
            case LIGHT_SCALE_AND_ALPHA:
                animateLightScaleAndAlpha(view, enterOrExit, duration, delay, execOnEnd);
                break;
            case SLIDE_AND_ALPHA:
                animateSlideAndAlpha(view, enterOrExit, duration, delay, execOnEnd);
                break;
            case LIGHT_SLIDE_AND_ALPHA:
                animateLightSlideAndAlpha(view, enterOrExit, duration, delay, execOnEnd);
                break;
        }
    }


    /**
     * Animate the background color of a view
     */
    public static void animateBackgroundColor(final View view, long duration, @ColorInt final int colorStart, @ColorInt final int colorEnd) {
        if (DEBUG) {
            Log.d(TAG, "animateBackgroundColor() called with: view = [" + view + "], duration = [" + duration + "], colorStart = [" + colorStart + "], colorEnd = [" + colorEnd + "]");
        }

        final int[][] EMPTY = new int[][]{new int[0]};
        ValueAnimator viewPropertyAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorStart, colorEnd);
        viewPropertyAnimator.setInterpolator(new FastOutSlowInInterpolator());
        viewPropertyAnimator.setDuration(duration);
        viewPropertyAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewCompat.setBackgroundTintList(view, new ColorStateList(EMPTY, new int[]{(int) animation.getAnimatedValue()}));
            }
        });
        viewPropertyAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ViewCompat.setBackgroundTintList(view, new ColorStateList(EMPTY, new int[]{colorEnd}));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationEnd(animation);
            }
        });
        viewPropertyAnimator.start();
    }

    /**
     * Animate the text color of any view that extends {@link TextView} (Buttons, EditText...)
     */
    public static void animateTextColor(final TextView view, long duration, @ColorInt final int colorStart, @ColorInt final int colorEnd) {
        if (DEBUG) {
            Log.d(TAG, "animateTextColor() called with: view = [" + view + "], duration = [" + duration + "], colorStart = [" + colorStart + "], colorEnd = [" + colorEnd + "]");
        }

        ValueAnimator viewPropertyAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorStart, colorEnd);
        viewPropertyAnimator.setInterpolator(new FastOutSlowInInterpolator());
        viewPropertyAnimator.setDuration(duration);
        viewPropertyAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTextColor((int) animation.getAnimatedValue());
            }
        });
        viewPropertyAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setTextColor(colorEnd);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setTextColor(colorEnd);
            }
        });
        viewPropertyAnimator.start();
    }

    public static ValueAnimator animateHeight(final View view, long duration, int targetHeight) {
        final int height = view.getHeight();
        if (DEBUG) {
            Log.d(TAG, "animateHeight: duration = [" + duration + "], from " + height + " to → " + targetHeight + " in: " + view);
        }

        ValueAnimator animator = ValueAnimator.ofFloat(height, targetHeight);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            final float value = (float) animation.getAnimatedValue();
            view.getLayoutParams().height = (int) value;
            view.requestLayout();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.getLayoutParams().height = targetHeight;
                view.requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.getLayoutParams().height = targetHeight;
                view.requestLayout();
            }
        });
        animator.start();

        return animator;
    }

    public static void animateRotation(final View view, long duration, int targetRotation) {
        if (DEBUG) {
            Log.d(TAG, "animateRotation: duration = [" + duration + "], from " + view.getRotation() + " to → " + targetRotation + " in: " + view);
        }
        view.animate().setListener(null).cancel();

        view.animate().rotation(targetRotation).setDuration(duration).setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        view.setRotation(targetRotation);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setRotation(targetRotation);
                    }
                }).start();
    }

    /*//////////////////////////////////////////////////////////////////////////
    // Internals
    //////////////////////////////////////////////////////////////////////////*/

    private static void animateAlpha(final View view, boolean enterOrExit, long duration, long delay, final Runnable execOnEnd) {
        if (enterOrExit) {
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(1f)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        } else {
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(0f)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        }
    }

    private static void animateScaleAndAlpha(final View view, boolean enterOrExit, long duration, long delay, final Runnable execOnEnd) {
        if (enterOrExit) {
            view.setScaleX(.8f);
            view.setScaleY(.8f);
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        } else {
            view.setScaleX(1f);
            view.setScaleY(1f);
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(0f).scaleX(.8f).scaleY(.8f)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        }
    }

    private static void animateLightScaleAndAlpha(final View view, boolean enterOrExit, long duration, long delay, final Runnable execOnEnd) {
        if (enterOrExit) {
            view.setAlpha(.5f);
            view.setScaleX(.95f);
            view.setScaleY(.95f);
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        } else {
            view.setAlpha(1f);
            view.setScaleX(1f);
            view.setScaleY(1f);
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(0f).scaleX(.95f).scaleY(.95f)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        }
    }

    private static void animateSlideAndAlpha(final View view, boolean enterOrExit, long duration, long delay, final Runnable execOnEnd) {
        if (enterOrExit) {
            view.setTranslationY(-view.getHeight());
            view.setAlpha(0f);
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(1f).translationY(0)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        } else {
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(0f).translationY(-view.getHeight())
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        }
    }

    private static void animateLightSlideAndAlpha(final View view, boolean enterOrExit, long duration, long delay, final Runnable execOnEnd) {
        if (enterOrExit) {
            view.setTranslationY(-view.getHeight() / 2);
            view.setAlpha(0f);
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(1f).translationY(0)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        } else {
            view.animate().setInterpolator(new FastOutSlowInInterpolator()).alpha(0f).translationY(-view.getHeight() / 2)
                    .setDuration(duration).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    if (execOnEnd != null) execOnEnd.run();
                }
            }).start();
        }
    }
}
