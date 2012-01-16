package android.view.ext;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.ext.R;

/**
 * Factory class for creating satellite in/out animations
 * 
 * @author Siyamed SINIR
 *
 */
public class SatelliteAnimationCreator {
    
    public static Animation createItemInAnimation(Context context, int index, long expandDuration, int x, int y){        
        RotateAnimation rotate = new RotateAnimation(720, 0, 
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        
        rotate.setInterpolator(context, R.anim.sat_item_in_rotate_interpolator);
        rotate.setDuration(expandDuration);
        
        TranslateAnimation translate = new TranslateAnimation(x, 0, y, 0);
        
        
        long delay = 250;
        if(expandDuration <= 250){
            delay = expandDuration / 3;
        }         
        
        long duration = 400;
        if((expandDuration-delay) > duration){
        	duration = expandDuration-delay; 
        }
        
        translate.setDuration(duration);
        translate.setStartOffset(delay);        
        
        translate.setInterpolator(context, R.anim.sat_item_anticipate_interpolator);
        
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        long alphaDuration = 10;
        if(expandDuration < 10){
        	alphaDuration = expandDuration / 10;
        }
        alphaAnimation.setDuration(alphaDuration);
        alphaAnimation.setStartOffset((delay + duration) - alphaDuration);
        
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(false);
        animationSet.setFillBefore(true);
        animationSet.setFillEnabled(true);
        
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotate);
        animationSet.addAnimation(translate);
        
        
        animationSet.setStartOffset(30*index);
        animationSet.start();
        animationSet.startNow();
        return animationSet;
    }
    
    public static Animation createItemOutAnimation(Context context, int index, long expandDuration, int x, int y){
    	
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        long alphaDuration = 60;
        if(expandDuration < 60){
        	alphaDuration = expandDuration / 4;
        }
        alphaAnimation.setDuration(alphaDuration);
        alphaAnimation.setStartOffset(0);

        
        TranslateAnimation translate = new TranslateAnimation(0, x, 0, y);
         
        translate.setStartOffset(0);
        translate.setDuration(expandDuration);        
        translate.setInterpolator(context, R.anim.sat_item_overshoot_interpolator);
        
        RotateAnimation rotate = new RotateAnimation(0f, 360f, 
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        
        rotate.setInterpolator(context, R.anim.sat_item_out_rotate_interpolator);
        
        long duration = 100;
        if(expandDuration <= 150){
            duration = expandDuration / 3;
        }
        
        rotate.setDuration(expandDuration-duration);
        rotate.setStartOffset(duration);        
        
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(false);
        animationSet.setFillBefore(true);
        animationSet.setFillEnabled(true);
                
        //animationSet.addAnimation(alphaAnimation);
        //animationSet.addAnimation(rotate);
        animationSet.addAnimation(translate);
        
        animationSet.setStartOffset(30*index);
        
        return animationSet;
    }
    
    public static Animation createMainButtonAnimation(Context context){
    	return AnimationUtils.loadAnimation(context, R.anim.sat_main_rotate_left);
    }
    
    public static Animation createMainButtonInverseAnimation(Context context){
    	return AnimationUtils.loadAnimation(context, R.anim.sat_main_rotate_right);
    }
    
    public static Animation createItemClickAnimation(Context context){
    	return AnimationUtils.loadAnimation(context, R.anim.sat_item_anim_click);
    }

    
    public static int getTranslateX(float degree, int distance){
        return Double.valueOf(distance * Math.cos(Math.toRadians(degree))).intValue();
    }
    
    public static int getTranslateY(float degree, int distance){
        return Double.valueOf(-1 * distance * Math.sin(Math.toRadians(degree))).intValue();
    }

}
