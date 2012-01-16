package android.view.ext;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Menu Item. 
 *
 * TODO: tell about usage
 * 
 * @author Siyamed SINIR
 *
 */
public class SatelliteMenuItem {
    private int id;
    private int imgResourceId;
    private Drawable imgDrawable;
    private ImageView view;
    private ImageView cloneView;
    private Animation outAnimation;
    private Animation inAnimation;
    private Animation clickAnimation;
    private int finalX;
    private int finalY;

    public SatelliteMenuItem(int id, int imgResourceId) {
        this.imgResourceId = imgResourceId;
        this.id = id;
    }    
    
    public SatelliteMenuItem(int id, Drawable imgDrawable) {
        this.imgDrawable = imgDrawable;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }

    public Drawable getImgDrawable() {
        return imgDrawable;
    }

    public void setImgDrawable(Drawable imgDrawable) {
        this.imgDrawable = imgDrawable;
    }    
    
    void setView(ImageView view) {
        this.view = view;
    }
    
    ImageView getView() {
        return view;
    }
    
    void setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
    }
    
    Animation getInAnimation() {
        return inAnimation;
    }
    
    void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }
    
    Animation getOutAnimation() {
        return outAnimation;
    }
    
    void setFinalX(int finalX) {
		this.finalX = finalX;
	}
    
    void setFinalY(int finalY) {
		this.finalY = finalY;
	}
    
    int getFinalX() {
		return finalX;
	}
    
    int getFinalY() {
		return finalY;
	}
    
    void setCloneView(ImageView cloneView) {
		this.cloneView = cloneView;
	}
    
    ImageView getCloneView() {
		return cloneView;
	}

	void setClickAnimation(Animation clickAnim) {
		this.clickAnimation = clickAnim;		
	}    
	
	Animation getClickAnimation() {
		return clickAnimation;
	}
}