/*
* Copyright (C) 2015 Benzo Rom
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.settings.benzo;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SlimSeekBarPreference;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.util.slim.Action;
import com.android.internal.logging.MetricsLogger;

public class NavbarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String DIM_NAV_BUTTONS_TIMEOUT = "dim_nav_buttons_timeout";
    private static final String DIM_NAV_BUTTONS_ALPHA = "dim_nav_buttons_alpha";
    private static final String DIM_NAV_BUTTONS_ANIMATE_DURATION = "dim_nav_buttons_animate_duration";

    private SlimSeekBarPreference mDimNavButtonsTimeout;
    private SlimSeekBarPreference mDimNavButtonsAlpha;
    private SlimSeekBarPreference mDimNavButtonsAnimateDuration;

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DEVELOPMENT;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.navbar_settings);

        // SlimDim

        mDimNavButtonsTimeout = (SlimSeekBarPreference) findPreference(DIM_NAV_BUTTONS_TIMEOUT);
        mDimNavButtonsTimeout.setDefault(3000);
        mDimNavButtonsTimeout.isMilliseconds(true);
        mDimNavButtonsTimeout.setInterval(1);
        mDimNavButtonsTimeout.minimumValue(100);
        mDimNavButtonsTimeout.multiplyValue(100);
        mDimNavButtonsTimeout.setOnPreferenceChangeListener(this);

        mDimNavButtonsAlpha = (SlimSeekBarPreference) findPreference(DIM_NAV_BUTTONS_ALPHA);
        mDimNavButtonsAlpha.setDefault(50);
        mDimNavButtonsAlpha.setInterval(1);
        mDimNavButtonsAlpha.setOnPreferenceChangeListener(this);

        mDimNavButtonsAnimateDuration = (SlimSeekBarPreference) findPreference(DIM_NAV_BUTTONS_ANIMATE_DURATION);
        mDimNavButtonsAnimateDuration.setDefault(2000);
        mDimNavButtonsAnimateDuration.isMilliseconds(true);
        mDimNavButtonsAnimateDuration.setInterval(1);
        mDimNavButtonsAnimateDuration.minimumValue(100);
        mDimNavButtonsAnimateDuration.multiplyValue(100);
        mDimNavButtonsAnimateDuration.setOnPreferenceChangeListener(this);
        updateSettings();
    }

    private void updateSettings() {
        boolean enableNavigationBar = Action.isNavBarEnabled(getActivity());
        if (mDimNavButtonsTimeout != null) {
            final int dimTimeout = Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_TIMEOUT, 3000);
            // minimum 100 is 1 interval of the 100 multiplier
            mDimNavButtonsTimeout.setInitValue((dimTimeout / 100) - 1);
        }

        if (mDimNavButtonsAlpha != null) {
            int alphaScale = Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_ALPHA, 50);
            mDimNavButtonsAlpha.setInitValue(alphaScale);
        }

        if (mDimNavButtonsAnimateDuration != null) {
            final int animateDuration = Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_ANIMATE_DURATION, 2000);
            // minimum 100 is 1 interval of the 100 multiplier
            mDimNavButtonsAnimateDuration.setInitValue((animateDuration / 100) - 1);
        }

        updateNavbarPreferences(enableNavigationBar);
    }

    private void updateNavbarPreferences(boolean show) {
        mDimNavButtonsTimeout.setEnabled(show);
        mDimNavButtonsAlpha.setEnabled(show);
        mDimNavButtonsAnimateDuration.setEnabled(show);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mDimNavButtonsTimeout) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_TIMEOUT, Integer.parseInt((String) newValue));
            return true;
        } else if (preference == mDimNavButtonsAlpha) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_ALPHA, Integer.parseInt((String) newValue));
            return true;
        } else if (preference == mDimNavButtonsAnimateDuration) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_ANIMATE_DURATION,
                Integer.parseInt((String) newValue));
            return true;
        }
         return false;
    }
}
