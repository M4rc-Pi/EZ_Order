package cat.inspedralbes.projecte2dam.ezorderv2.utilities;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import cat.inspedralbes.projecte2dam.ezorderv2.R;

public class CustomFloatingActionButton extends com.google.android.material.floatingactionbutton.FloatingActionButton {

    Animation fabOpen, fabClose;

    public CustomFloatingActionButton(@NonNull Context context) {
        super(context);
        fabOpen = AnimationUtils.loadAnimation(context, R.anim.animation_fab_open);
        fabClose = AnimationUtils.loadAnimation(context, R.anim.animation_fab_close);
    }

    public void showFAB(){
        this.startAnimation(fabOpen);
    }

    public void hideFAB(){
        this.startAnimation(fabClose);
    }

}
