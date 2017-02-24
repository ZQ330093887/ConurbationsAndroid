package com.test.admin.conurbations.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.AnimationUtils;


public class MaterialSearchView extends FrameLayout {

    /**
     * The maximum number of results we want to return from the voice recognition.
     */
    private static final int MAX_RESULTS = 1;

    /**
     * The identifier for the voice request intent. (Guess why it's 42).
     */
    public static final int REQUEST_VOICE = 42;

    /**
     * Whether or not the search view is open right now.
     */
    private boolean mOpen;

    /**
     * The Context that this view appears in.
     */
    private Context mContext;

    /**
     * Whether or not the MaterialSearchView will animate into view or just appear.
     */
    private boolean mShouldAnimate;

    /**
     * Flag for whether or not we are clearing focus.
     */
    private boolean mClearingFocus;

    /**
     * Voice hint prompt text.
     */
    private String mHintPrompt;
    //endregion

    //region UI Elements
    /**
     * The tint that appears over the search view.
     */
    private View mTintView;

    /**
     * The root of the search view.
     */
    private FrameLayout mRoot;

    /**
     * The bar at the top of the SearchView containing the EditText and ImageButtons.
     */
    private LinearLayout mSearchBar;

    /**
     * The EditText for entering a search.
     */
    private EditText mSearchEditText;

    /**
     * The ImageButton for navigating back.
     */
    private ImageButton mBack;

    /**
     * The ImageButton for clearing the search text.
     */
    private ImageButton mClear;

    /**
     * The current query text.
     */
    private CharSequence mCurrentQuery;
    //endregion

    //region Listeners
    /**
     * Listener for when the query text is submitted or changed.
     */
    private OnQueryTextListener mOnQueryTextListener;

    /**
     * Listener for when the search view opens and closes.
     */
    private SearchViewListener mSearchViewListener;

    //region Constructors
    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MaterialSearchView(Context context, AttributeSet attributeSet, int defStyleAttributes) {
        super(context, attributeSet);
        this.mContext = context;
        this.mShouldAnimate = true;
        init();
        initStyle(attributeSet, defStyleAttributes);
    }

    /**
     * Preforms any required initializations for the search view.
     */
    private void init() {
        // Inflate view
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this, true);

        // Get items
        mRoot = (FrameLayout) findViewById(R.id.search_layout);
        mTintView = mRoot.findViewById(R.id.transparent_view);
        mSearchBar = (LinearLayout) mRoot.findViewById(R.id.search_bar);
        mBack = (ImageButton) mRoot.findViewById(R.id.action_back);
        mSearchEditText = (EditText) mRoot.findViewById(R.id.et_search);
        mClear = (ImageButton) mRoot.findViewById(R.id.action_clear);

        // Set click listeners
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch();
            }
        });
        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                    closeSearch();
                }
            }
        });

        mClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
            }
        });

        // Initialize the search view.
        initSearchView();
    }

    /**
     * Initializes the style of this view.
     *
     * @param attributeSet      The attributes to apply to the view.
     * @param defStyleAttribute An attribute to the style theme applied to this view.
     */
    private void initStyle(AttributeSet attributeSet, int defStyleAttribute) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attributeSet, R.styleable.MaterialSearchView, defStyleAttribute, 0);

        if (typedArray != null) {
            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchBackground)) {
                setBackground(typedArray.getDrawable(R.styleable.MaterialSearchView_searchBackground));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_android_textColor)) {
                setTextColor(typedArray.getColor(R.styleable.MaterialSearchView_android_textColor,
                        ContextCompat.getColor(mContext, R.color.black)));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_android_textColorHint)) {
                setHintTextColor(typedArray.getColor(R.styleable.MaterialSearchView_android_textColorHint,
                        ContextCompat.getColor(mContext, R.color.gray_50)));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_android_hint)) {
                setHint(typedArray.getString(R.styleable.MaterialSearchView_android_hint));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchCloseIcon)) {
                setClearIcon(typedArray.getDrawable(R.styleable.MaterialSearchView_searchCloseIcon));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchBackIcon)) {
                setBackIcon(typedArray.getDrawable(R.styleable.MaterialSearchView_searchBackIcon));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_android_inputType)) {
                setInputType(typedArray.getInteger(R.styleable.MaterialSearchView_android_inputType, InputType.TYPE_CLASS_TEXT));
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_searchBarHeight)) {
                setSearchBarHeight(typedArray.getDimensionPixelSize(R.styleable.MaterialSearchView_searchBarHeight, getAppCompatActionBarHeight()));
            } else {
                setSearchBarHeight(getAppCompatActionBarHeight());
            }

            if (typedArray.hasValue(R.styleable.MaterialSearchView_voiceHintPrompt)) {
                setVoiceHintPrompt(typedArray.getString(R.styleable.MaterialSearchView_voiceHintPrompt));
            } else {
                setVoiceHintPrompt(mContext.getString(R.string.hint_prompt));
            }

            ViewCompat.setFitsSystemWindows(this, typedArray.getBoolean(R.styleable.MaterialSearchView_android_fitsSystemWindows, false));

            typedArray.recycle();
        }
    }

    /**
     * Preforms necessary initializations on the SearchView.
     */
    private void initSearchView() {
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // When an edit occurs, submit the query.
                onSubmitQuery();
                return true;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When the text changes, filter
                MaterialSearchView.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If we gain focus, show keyboard and show suggestions.
                if (hasFocus) {
                    showKeyboard(mSearchEditText);
                }
            }
        });
    }
    //endregion

    //region Show Methods

    /**
     * Displays the keyboard with a focus on the Search EditText.
     *
     * @param view The view to attach the keyboard to.
     */
    private void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }

        view.requestFocus();

        if (!isHardKeyboardAvailable()) {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    /**
     * Method that checks if there's a physical keyboard on the phone.
     *
     * @return true if there's a physical keyboard connected, false otherwise.
     */
    private boolean isHardKeyboardAvailable() {
        return mContext.getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS;
    }

    /**
     * Changes the visibility of the clear button to VISIBLE or GONE.
     *
     * @param display True to display the clear button, false to hide it.
     */
    private void displayClearButton(boolean display) {
        mClear.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    /**
     * Displays the SearchView.
     */
    public void openSearch() {
        // If search is already open, just return.
        if (mOpen) {
            return;
        }

        // Get focus
        mSearchEditText.setText("");
        mSearchEditText.requestFocus();

        if (mShouldAnimate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mRoot.setVisibility(View.VISIBLE);
                AnimationUtils.circleRevealView(mSearchBar);
            } else {
                AnimationUtils.fadeInView(mRoot);
            }

        } else {
            mRoot.setVisibility(View.VISIBLE);
        }

        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewOpened();
        }

        mOpen = true;
    }

    /**
     * Hides the keyboard displayed for the SearchEditText.
     *
     * @param view The view to detach the keyboard from.
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Closes the search view if necessary.
     */
    public void closeSearch() {
        // If we're already closed, just return.
        if (!mOpen) {
            return;
        }

        // Clear text, values, and focus.
        mSearchEditText.setText("");
        clearFocus();

        if (mShouldAnimate) {
            final View v = mRoot;

            AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // After the animation is done. Hide the root view.
                    v.setVisibility(View.GONE);
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AnimationUtils.circleHideView(mSearchBar, listenerAdapter);
            } else {
                AnimationUtils.fadeOutView(mRoot);
            }
        } else {
            // Just hide the view.
            mRoot.setVisibility(View.GONE);
        }


        // Call listener if we have one
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewClosed();
        }

        mOpen = false;
    }
    //endregion

    //region Interface Methods

    /**
     * Filters and updates the buttons when text is changed.
     *
     * @param newText The new text.
     */
    private void onTextChanged(CharSequence newText) {
        // Get current query
        mCurrentQuery = mSearchEditText.getText();

        // If the text is not empty, show the empty button and hide the voice button
        if (!TextUtils.isEmpty(mCurrentQuery)) {
            displayClearButton(true);
        } else {
            displayClearButton(false);
        }

        // If we have a query listener and the text has changed, call it.
        if (mOnQueryTextListener != null) {
            mOnQueryTextListener.onQueryTextChange(newText.toString());
        }
    }

    /**
     * Called when a query is submitted. This will close the search view.
     */
    private void onSubmitQuery() {
        // Get the query.
        CharSequence query = mSearchEditText.getText();

        // If the query is not null and it has some text, submit it.
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {

            // If we don't have a listener, or if the search view handled the query, close it.
            // TODO - Improve.
            if (mOnQueryTextListener == null || !mOnQueryTextListener.onQueryTextSubmit(query.toString())) {
                closeSearch();
                mSearchEditText.setText("");
            }
        }
    }
    //endregion

    //region Mutators
    public void setOnQueryTextListener(OnQueryTextListener mOnQueryTextListener) {
        this.mOnQueryTextListener = mOnQueryTextListener;
    }

    public void setSearchViewListener(SearchViewListener mSearchViewListener) {
        this.mSearchViewListener = mSearchViewListener;
    }

    /**
     * Sets the background of the SearchView.
     *
     * @param background The drawable to use as a background.
     */
    @Override
    public void setBackground(Drawable background) {
        // Method changed in jelly bean for setting background.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSearchBar.setBackground(background);
        } else {
            //noinspection deprecation
            mSearchBar.setBackgroundDrawable(background);
        }
    }

    /**
     * Sets the background color of the SearchView.
     *
     * @param color The color to use for the background.
     */
    @Override
    public void setBackgroundColor(int color) {
        // Set background color of search bar.
        mSearchBar.setBackgroundColor(color);
    }

    /**
     * Adjust the background tint alpha, based on a percentage.
     *
     * @param factor The factor of the alpha, from 0% to 100%.
     */
    public void adjustTintAlpha(float factor) {
        if (factor < 0 || factor > 1.0) return;

        Drawable d = mTintView.getBackground();

        if (d instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) d;
            int color = cd.getColor();

            color = adjustAlpha(color, factor);

            mTintView.setBackgroundColor(color);
        }
    }

    /**
     * Adjust the alpha of a color based on a percent factor.
     *
     * @param color  - The color you want to change the alpha value.
     * @param factor - The factor of the alpha, from 0% to 100%.
     * @return The color with the adjusted alpha value.
     */
    private int adjustAlpha(int color, float factor) {
        if (factor < 0) return color;

        int alpha = Math.round(Color.alpha(color) * factor);

        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Sets the text color of the EditText.
     *
     * @param color The color to use for the EditText.
     */
    public void setTextColor(int color) {
        mSearchEditText.setTextColor(color);
    }

    /**
     * Sets the text color of the search hint.
     *
     * @param color The color to be used for the hint text.
     */
    public void setHintTextColor(int color) {
        mSearchEditText.setHintTextColor(color);
    }

    /**
     * Sets the hint to be used for the search EditText.
     *
     * @param hint The hint to be displayed in the search EditText.
     */
    public void setHint(CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    /**
     * Sets the icon for the clear action.
     *
     * @param drawable The drawable to represent the clear action.
     */
    public void setClearIcon(Drawable drawable) {
        mClear.setImageDrawable(drawable);
    }

    /**
     * Sets the icon for the back action.
     *
     * @param drawable The drawable to represent the back action.
     */
    public void setBackIcon(Drawable drawable) {
        mBack.setImageDrawable(drawable);
    }

    /**
     * Sets the input type of the SearchEditText.
     *
     * @param inputType The input type to set to the EditText.
     */
    public void setInputType(int inputType) {
        mSearchEditText.setInputType(inputType);
    }

    /**
     * Sets the bar height if prefered to not use the existing actionbar height value
     *
     * @param height The value of the height in pixels
     */
    public void setSearchBarHeight(final int height) {
        mSearchBar.setMinimumHeight(height);
        mSearchBar.getLayoutParams().height = height;
    }

    public void setVoiceHintPrompt(final String hintPrompt) {
        if (!TextUtils.isEmpty(hintPrompt)) {
            mHintPrompt = hintPrompt;
        } else {
            mHintPrompt = mContext.getString(R.string.hint_prompt);
        }
    }

    /**
     * Returns the actual AppCompat ActionBar height value. This will be used as the default
     *
     * @return The value of the actual actionbar height in pixels
     */
    private int getAppCompatActionBarHeight() {
        TypedValue tv = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true);
        return getResources().getDimensionPixelSize(tv.resourceId);
    }
    //endregion

    //region Accessors

    /**
     * Determines if the search view is opened or closed.
     *
     * @return True if the search view is open, false if it is closed.
     */
    public boolean isOpen() {
        return mOpen;
    }

    //region View Methods

    /**
     * Handles any cleanup when focus is cleared from the view.
     */
    @Override
    public void clearFocus() {
        this.mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchEditText.clearFocus();
        this.mClearingFocus = false;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        // Don't accept if we are clearing focus, or if the view isn't focusable.
        return !(mClearingFocus || !isFocusable()) && mSearchEditText.requestFocus(direction, previouslyFocusedRect);
    }

    //endregion

    //region Interfaces

    /**
     * Interface that handles the submission and change of search queries.
     */
    public interface OnQueryTextListener {
        /**
         * Called when a search query is submitted.
         *
         * @param query The text that will be searched.
         * @return True when the query is handled by the listener, false to let the SearchView handle the default case.
         */
        boolean onQueryTextSubmit(String query);

        /**
         * Called when a search query is changed.
         *
         * @param newText The new text of the search query.
         * @return True when the query is handled by the listener, false to let the SearchView handle the default case.
         */
        boolean onQueryTextChange(String newText);
    }

    /**
     * Interface that handles the opening and closing of the SearchView.
     */
    public interface SearchViewListener {
        /**
         * Called when the searchview is opened.
         */
        void onSearchViewOpened();

        /**
         * Called when the search view closes.
         */
        void onSearchViewClosed();
    }
}
