package com.example.medcords.internal;

import android.view.animation.Interpolator;


public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
