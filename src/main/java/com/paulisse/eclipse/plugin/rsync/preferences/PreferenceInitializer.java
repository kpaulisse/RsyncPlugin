package com.paulisse.eclipse.plugin.rsync.preferences;

import com.paulisse.eclipse.plugin.rsync.Activator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /**
   * Initialize the default preferences for plugin.
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    for (Preferences preference : Preferences.values()) {
      store.setDefault(preference.getVariableName(), preference.getDefaultValue());
    }
  }
}
