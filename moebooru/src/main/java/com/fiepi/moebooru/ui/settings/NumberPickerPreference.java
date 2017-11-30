package com.fiepi.moebooru.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import com.fiepi.moebooru.R;

import moe.shizuku.preference.DialogPreference;
import moe.shizuku.preference.PreferenceDialogFragment;

import static android.support.v4.content.res.TypedArrayUtils.*;

/**
 * Created by fiepi on 11/30/17.
 */

public class NumberPickerPreference extends DialogPreference {

    private NumberPicker mNumberPicker;
    private int mValue;
    private String mSummary;
    private boolean mValueSet;

    @SuppressLint("RestrictedApi")
    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mNumberPicker = new NumberPicker(context);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);
        mNumberPicker.setMinValue(array.getInt(R.styleable.NumberPickerPreference_min, 0));
        mNumberPicker.setMaxValue(array.getInt(R.styleable.NumberPickerPreference_max, 100));
        array = context.obtainStyledAttributes(attrs,
                R.styleable.Preference, defStyleAttr, defStyleRes);
        mSummary =  TypedArrayUtils.getString(array, R.styleable.Preference_summary,
                R.styleable.Preference_android_summary);
        array.recycle();
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public NumberPickerPreference(Context context) {
        this(context, null);
    }

    public NumberPicker getNumberPicker() {
        return mNumberPicker;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        // Always persist/notify the first time.
        final boolean changed = mValue != value;
        if (changed || !mValueSet) {
            mValue = value;
            mNumberPicker.setValue(value);
            persistInt(value);
            mValueSet = true;
            if (changed) {
                notifyChanged();
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mValue) : (Integer) defaultValue);
    }

    @NonNull
    @Override
    protected DialogFragment onCreateDialogFragment(String key) {
        final NumberPickerPreferenceDialogFragment
                fragment = new NumberPickerPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(PreferenceDialogFragment.ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public CharSequence getSummary() {
        final int entry = getValue();
        if (mSummary == null) {
            return super.getSummary();
        } else {
            return String.format(mSummary, entry);
        }
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        if (summary == null && mSummary != null) {
            mSummary = null;
        } else if (summary != null && !summary.equals(mSummary)) {
            mSummary = summary.toString();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState myState = new SavedState(superState);
        myState.value = mNumberPicker.getValue();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        final int value = myState.value;
        mNumberPicker.post(new Runnable() {
            @Override
            public void run() {
                mNumberPicker.setValue(value);
            }
        });
    }

    private static class SavedState extends BaseSavedState {
        int value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(value);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
