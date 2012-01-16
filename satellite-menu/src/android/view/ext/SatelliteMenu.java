package android.view.ext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Provides a "Path" like menu for android. ??
 * 
 * TODO: tell about usage
 * 
 * @author Siyamed SINIR
 * 
 */
public class SatelliteMenu extends FrameLayout {

	private static final int DEFAULT_SATELLITE_DISTANCE = 200;
	private static final float DEFAULT_TOTAL_SPACING_DEGREES = 90f;
	private static final boolean DEFAULT_CLOSE_ON_CLICK = true;
	private static final int DEFAULT_EXPAND_DURATION = 400;

	private Animation mainRotateRight;
	private Animation mainRotateLeft;

	private ImageView imgMain;
	private SateliteClickedListener itemClickedListener;
	private InternalSatelliteOnClickListener internalItemClickListener;

	private List<SatelliteMenuItem> menuItems = new ArrayList<SatelliteMenuItem>();
	private Map<View, SatelliteMenuItem> viewToItemMap = new HashMap<View, SatelliteMenuItem>();

	private AtomicBoolean plusAnimationActive = new AtomicBoolean(false);

	// ?? how to save/restore?
	private IDegreeProvider gapDegreesProvider = new DefaultDegreeProvider();

	//States of these variables are saved
	private boolean rotated = false;
	private int measureDiff = 0;
	//States of these variables are saved - Also configured from XML 
	private float totalSpacingDegree = DEFAULT_TOTAL_SPACING_DEGREES;
	private int satelliteDistance = DEFAULT_SATELLITE_DISTANCE;	
	private int expandDuration = DEFAULT_EXPAND_DURATION;
	private boolean closeItemsOnClick = DEFAULT_CLOSE_ON_CLICK;

	public SatelliteMenu(Context context) {
		super(context);
		init(context, null, 0);
	}

	public SatelliteMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public SatelliteMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		LayoutInflater.from(context).inflate(R.layout.sat_main, this, true);		
		imgMain = (ImageView) findViewById(R.id.sat_main);

		if(attrs != null){			
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SatelliteMenu, defStyle, 0);					
			satelliteDistance = typedArray.getDimensionPixelSize(R.styleable.SatelliteMenu_satelliteDistance, DEFAULT_SATELLITE_DISTANCE);
			totalSpacingDegree = typedArray.getFloat(R.styleable.SatelliteMenu_totalSpacingDegree, DEFAULT_TOTAL_SPACING_DEGREES);
			closeItemsOnClick = typedArray.getBoolean(R.styleable.SatelliteMenu_closeOnClick, DEFAULT_CLOSE_ON_CLICK);
			expandDuration = typedArray.getInt(R.styleable.SatelliteMenu_expandDuration, DEFAULT_EXPAND_DURATION);
			//float satelliteDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources().getDisplayMetrics());
			typedArray.recycle();
		}
		
		
		mainRotateLeft = SatelliteAnimationCreator.createMainButtonAnimation(context);
		mainRotateRight = SatelliteAnimationCreator.createMainButtonInverseAnimation(context);

		Animation.AnimationListener plusAnimationListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				plusAnimationActive.set(false);
			}
		};

		mainRotateLeft.setAnimationListener(plusAnimationListener);
		mainRotateRight.setAnimationListener(plusAnimationListener);

		imgMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SatelliteMenu.this.onClick();
			}
		});

		internalItemClickListener = new InternalSatelliteOnClickListener(this);
	}

	private void onClick() {
		if (plusAnimationActive.compareAndSet(false, true)) {
			if (!rotated) {
				imgMain.startAnimation(mainRotateLeft);
				for (SatelliteMenuItem item : menuItems) {
					item.getView().startAnimation(item.getOutAnimation());
				}
			} else {
				imgMain.startAnimation(mainRotateRight);
				for (SatelliteMenuItem item : menuItems) {
					item.getView().startAnimation(item.getInAnimation());
				}
			}
			rotated = !rotated;
		}
	}

	private void openItems() {
		if (plusAnimationActive.compareAndSet(false, true)) {
			if (!rotated) {
				imgMain.startAnimation(mainRotateLeft);
				for (SatelliteMenuItem item : menuItems) {
					item.getView().startAnimation(item.getOutAnimation());
				}
			}
			rotated = !rotated;
		}
	}

	private void closeItems() {
		if (plusAnimationActive.compareAndSet(false, true)) {
			if (rotated) {
				imgMain.startAnimation(mainRotateRight);
				for (SatelliteMenuItem item : menuItems) {
					item.getView().startAnimation(item.getInAnimation());
				}
			}
			rotated = !rotated;
		}
	}

	public void addItems(List<SatelliteMenuItem> items) {

		menuItems.addAll(items);
		this.removeView(imgMain);
		TextView tmpView = new TextView(getContext());
		tmpView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));

		float[] degrees = getDegrees(menuItems.size());
		int index = 0;
		for (SatelliteMenuItem menuItem : menuItems) {
			int finalX = SatelliteAnimationCreator.getTranslateX(
					degrees[index], satelliteDistance);
			int finalY = SatelliteAnimationCreator.getTranslateY(
					degrees[index], satelliteDistance);

			ImageView itemView = (ImageView) LayoutInflater.from(getContext())
					.inflate(R.layout.sat_item_cr, this, false);
			ImageView cloneView = (ImageView) LayoutInflater.from(getContext())
					.inflate(R.layout.sat_item_cr, this, false);
			itemView.setTag(menuItem.getId());
			cloneView.setVisibility(View.GONE);
			itemView.setVisibility(View.GONE);

			cloneView.setOnClickListener(internalItemClickListener);
			cloneView.setTag(Integer.valueOf(menuItem.getId()));
			FrameLayout.LayoutParams layoutParams = getLayoutParams(cloneView);
			layoutParams.bottomMargin = Math.abs(finalY);
			layoutParams.leftMargin = Math.abs(finalX);
			cloneView.setLayoutParams(layoutParams);

			if (menuItem.getImgResourceId() > 0) {
				itemView.setImageResource(menuItem.getImgResourceId());
				cloneView.setImageResource(menuItem.getImgResourceId());
			} else if (menuItem.getImgDrawable() != null) {
				itemView.setImageDrawable(menuItem.getImgDrawable());
				cloneView.setImageDrawable(menuItem.getImgDrawable());
			}

			Animation itemOut = SatelliteAnimationCreator.createItemOutAnimation(getContext(), index,expandDuration, finalX, finalY);
			Animation itemIn = SatelliteAnimationCreator.createItemInAnimation(getContext(), index, expandDuration, finalX, finalY);
			Animation itemClick = SatelliteAnimationCreator.createItemClickAnimation(getContext());

			menuItem.setView(itemView);
			menuItem.setCloneView(cloneView);
			menuItem.setInAnimation(itemIn);
			menuItem.setOutAnimation(itemOut);
			menuItem.setClickAnimation(itemClick);
			menuItem.setFinalX(finalX);
			menuItem.setFinalY(finalY);

			itemIn.setAnimationListener(new SatelliteAnimationListener(itemView, true, viewToItemMap));
			itemOut.setAnimationListener(new SatelliteAnimationListener(itemView, false, viewToItemMap));
			itemClick.setAnimationListener(new SatelliteItemClickAnimationListener(this, menuItem.getId()));
			
			this.addView(itemView);
			this.addView(cloneView);
			viewToItemMap.put(itemView, menuItem);
			viewToItemMap.put(cloneView, menuItem);
			index++;
		}

		this.addView(imgMain);
	}

	private float[] getDegrees(int count) {
		return gapDegreesProvider.getDegrees(count, totalSpacingDegree);
	}

	private void recalculateMeasureDiff() {
		int itemWidth = 0;
		if (menuItems.size() > 0) {
			itemWidth = menuItems.get(0).getView().getWidth();
		}
		measureDiff = Float.valueOf(satelliteDistance * 0.2f).intValue()
				+ itemWidth;
	}

	private void resetItems() {
		if (menuItems.size() > 0) {
			List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>(
					menuItems);
			menuItems.clear();
			this.removeAllViews();
			addItems(items);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		recalculateMeasureDiff();

		int totalHeight = imgMain.getHeight() + satelliteDistance + measureDiff;
		int totalWidth = imgMain.getWidth() + satelliteDistance + measureDiff;
		setMeasuredDimension(totalWidth, totalHeight);
	}

	private static class SatelliteItemClickAnimationListener implements Animation.AnimationListener {
		private WeakReference<SatelliteMenu> menuRef;
		private int tag;
		
		public SatelliteItemClickAnimationListener(SatelliteMenu menu, int tag) {
			this.menuRef = new WeakReference<SatelliteMenu>(menu);
			this.tag = tag;
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationStart(Animation animation) {
			SatelliteMenu menu = menuRef.get();
			if(menu != null && menu.closeItemsOnClick){
				menu.close();
				if(menu.itemClickedListener != null){
					menu.itemClickedListener.eventOccured(tag);
				}
			}
		}		
	}
	
	private static class SatelliteAnimationListener implements Animation.AnimationListener {
		private WeakReference<View> viewRef;
		private boolean isInAnimation;
		private Map<View, SatelliteMenuItem> viewToItemMap;

		public SatelliteAnimationListener(View view, boolean isIn, Map<View, SatelliteMenuItem> viewToItemMap) {
			this.viewRef = new WeakReference<View>(view);
			this.isInAnimation = isIn;
			this.viewToItemMap = viewToItemMap;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (viewRef != null) {
				View view = viewRef.get();
				if (view != null) {
					SatelliteMenuItem menuItem = viewToItemMap.get(view);
					if (isInAnimation) {
						menuItem.getView().setVisibility(View.VISIBLE);
						menuItem.getCloneView().setVisibility(View.GONE);
					} else {
						menuItem.getCloneView().setVisibility(View.GONE);
						menuItem.getView().setVisibility(View.VISIBLE);
					}
				}
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (viewRef != null) {
				View view = viewRef.get();
				if (view != null) {
					SatelliteMenuItem menuItem = viewToItemMap.get(view);

					if (isInAnimation) {
						menuItem.getView().setVisibility(View.GONE);
						menuItem.getCloneView().setVisibility(View.GONE);
					} else {
						menuItem.getCloneView().setVisibility(View.VISIBLE);
						menuItem.getView().setVisibility(View.GONE);
					}
				}
			}
		}
	}

	public Map<View, SatelliteMenuItem> getViewToItemMap() {
		return viewToItemMap;
	}
	
	private static FrameLayout.LayoutParams getLayoutParams(View view) {
		return (FrameLayout.LayoutParams) view.getLayoutParams();
	}

	private static class InternalSatelliteOnClickListener implements View.OnClickListener {
		private WeakReference<SatelliteMenu> menuRef;
		
		public InternalSatelliteOnClickListener(SatelliteMenu menu) {
			this.menuRef = new WeakReference<SatelliteMenu>(menu);
		}

		@Override
		public void onClick(View v) {
			SatelliteMenu menu = menuRef.get();
			if(menu != null){
				SatelliteMenuItem menuItem = menu.getViewToItemMap().get(v);
				v.startAnimation(menuItem.getClickAnimation());	
			}
		}
	}
	
	/**
	 * Sets the click listener for satellite items.
	 * 
	 * @param itemClickedListener
	 */
	public void setOnItemClickedListener(SateliteClickedListener itemClickedListener) {
		this.itemClickedListener = itemClickedListener;
	}

	
	/**
	 * Defines the algorithm to define the gap between each item. 
	 * Note: Calling before adding items is strongly recommended. 
	 * 
	 * @param gapDegreeProvider
	 */
	public void setGapDegreeProvider(IDegreeProvider gapDegreeProvider) {
		this.gapDegreesProvider = gapDegreeProvider;
		resetItems();
	}

	/**
	 * Defines the total space between the initial and final item in degrees.
	 * Note: Calling before adding items is strongly recommended.
	 * 
	 * @param totalSpacingDegree The degree between initial and final items. 
	 */
	public void setTotalSpacingDegree(float totalSpacingDegree) {
		this.totalSpacingDegree = totalSpacingDegree;
		resetItems();
	}

	/**
	 * Sets the distance of items from the center in pixels.
	 * Note: Calling before adding items is strongly recommended.
	 * 
	 * @param distance the distance of items to center in pixels.
	 */
	public void setSatelliteDistance(int distance) {
		this.satelliteDistance = distance;
		resetItems();
	}

	/**
	 * Sets the duration for expanding and collapsing the items in miliseconds. 
	 * Note: Calling before adding items is strongly recommended.
	 * 
	 * @param expandDuration the duration for expanding and collapsing the items in miliseconds.
	 */
	public void setExpandDuration(int expandDuration) {
		this.expandDuration = expandDuration;
		resetItems();
	}
	
	/**
	 * Sets the image resource for the center button.
	 * 
	 * @param resource The image resource.
	 */
	public void setMainImage(int resource) {
		this.imgMain.setImageResource(resource);
	}

	/**
	 * Sets the image drawable for the center button.
	 * 
	 * @param resource The image drawable.
	 */
	public void setMainImage(Drawable drawable) {
		this.imgMain.setImageDrawable(drawable);
	}

	/**
	 * Defines if the menu shall collapse the items when an item is clicked. Default value is true. 
	 * 
	 * @param closeItemsOnClick
	 */
	public void setCloseItemsOnClick(boolean closeItemsOnClick) {
		this.closeItemsOnClick = closeItemsOnClick;
	}
	
	/**
	 * The listener class for item click event. 
	 * @author Siyamed SINIR
	 */
	public interface SateliteClickedListener {
		/**
		 * When an item is clicked, informs with the id of the item, which is given while adding the items. 
		 * 
		 * @param id The id of the item. 
		 */
		public void eventOccured(int id);
	}

	/**
	 * Expand the menu items. 
	 */
	public void expand() {
		openItems();
	}

	/**
	 * Collapse the menu items
	 */
	public void close() {
		closeItems();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.rotated = rotated;
		ss.totalSpacingDegree = totalSpacingDegree;
		ss.satelliteDistance = satelliteDistance;
		ss.measureDiff = measureDiff;
		ss.expandDuration = expandDuration;
		ss.closeItemsOnClick = closeItemsOnClick;
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		rotated = ss.rotated;
		totalSpacingDegree = ss.totalSpacingDegree;
		satelliteDistance = ss.satelliteDistance;
		measureDiff = ss.measureDiff;
		expandDuration = ss.expandDuration;
		closeItemsOnClick = ss.closeItemsOnClick;

		super.onRestoreInstanceState(ss.getSuperState());
	}

	static class SavedState extends BaseSavedState {
		boolean rotated;
		private float totalSpacingDegree;
		private int satelliteDistance;
		private int measureDiff;
		private int expandDuration;
		private boolean closeItemsOnClick;
		
		SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel in) {
			super(in);
			rotated = Boolean.valueOf(in.readString());
			totalSpacingDegree = in.readFloat();
			satelliteDistance = in.readInt();
			measureDiff = in.readInt();
			expandDuration = in.readInt();
			closeItemsOnClick = Boolean.valueOf(in.readString());
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeString(Boolean.toString(rotated));
			out.writeFloat(totalSpacingDegree);
			out.writeInt(satelliteDistance);
			out.writeInt(measureDiff);
			out.writeInt(expandDuration);
			out.writeString(Boolean.toString(closeItemsOnClick));
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
