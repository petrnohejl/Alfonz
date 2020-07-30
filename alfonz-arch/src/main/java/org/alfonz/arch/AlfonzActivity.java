package org.alfonz.arch;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.alfonz.arch.widget.ToolbarIndicator;

import java.util.List;

public abstract class AlfonzActivity extends AppCompatActivity {
	private int mToolbarHashCode = 0;

	@Override
	public void onBackPressed() {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		boolean handled = false;

		for (Fragment fragment : fragments) {
			if (fragment instanceof AlfonzFragment) {
				handled = ((AlfonzFragment<?>) fragment).onBackPressed();
				if (handled) break;
			}
		}

		if (!handled) super.onBackPressed();
	}

	@Nullable
	public ActionBar setupActionBar(@NonNull ToolbarIndicator indicator) {
		return setupActionBar(indicator, null, null);
	}

	@Nullable
	public ActionBar setupActionBar(@NonNull ToolbarIndicator indicator, @Nullable CharSequence title) {
		return setupActionBar(indicator, title, null);
	}

	/**
	 * Setup main toolbar as ActionBar. Try to tint navigation icon based on toolbar's theme.
	 *
	 * @param indicator navigation icon (NONE, BACK, CLOSE are predefined). Uses toolbar theme color for tinting.
	 * @param title to be shown as in ActionBar. If it is null, title is not changed! Use empty string to clear it.
	 * @param toolbar may be null, in that case it is looking for R.id.toolbar.
	 * @return initialized ActionBar or null
	 */
	@Nullable
	public ActionBar setupActionBar(@NonNull ToolbarIndicator indicator, @Nullable CharSequence title, @Nullable Toolbar toolbar) {
		if (toolbar == null) {
			toolbar = findViewById(R.id.toolbar);
			if (toolbar == null) {
				throw new IllegalStateException("Toolbar not found. Add Toolbar with R.id.toolbar identifier in the activity layout or pass Toolbar as a parameter.");
			}
		}

		// this check is here because if 2 fragments with different indicators share a toolbar in activity,
		// it caused bug that back icon was not shown
		if (mToolbarHashCode != toolbar.hashCode()) {
			setSupportActionBar(toolbar);
		}

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(indicator.isHomeAsUpEnabled());
			actionBar.setHomeButtonEnabled(indicator.isHomeEnabled());

			if (indicator.getDrawableRes() == 0) {
				actionBar.setHomeAsUpIndicator(null);
			} else {
				Drawable iconDrawable = indicator.getTintedDrawable(toolbar);
				actionBar.setHomeAsUpIndicator(iconDrawable);
			}

			if (title != null) {
				actionBar.setTitle(title);
			}
		}

		mToolbarHashCode = toolbar.hashCode();
		return actionBar;
	}

	public void replaceFragment(@NonNull Fragment fragment) {
		replaceFragment(fragment, false, false, null);
	}

	public void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack, boolean allowStateLoss) {
		replaceFragment(fragment, addToBackStack, allowStateLoss, null);
	}

	public void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack, boolean allowStateLoss, @Nullable String tag) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment, tag);

		if (addToBackStack) {
			transaction.addToBackStack(fragment.getClass().getSimpleName());
		}

		if (allowStateLoss) {
			transaction.commitAllowingStateLoss();
		} else {
			transaction.commit();
		}
	}
}
