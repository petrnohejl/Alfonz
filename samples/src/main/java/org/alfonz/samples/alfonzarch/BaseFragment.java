package org.alfonz.samples.alfonzarch;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.ViewDataBinding;

import com.google.android.material.snackbar.Snackbar;

import org.alfonz.arch.AlfonzBindingFragment;
import org.alfonz.samples.alfonzarch.event.SnackbarEvent;
import org.alfonz.samples.alfonzarch.event.ToastEvent;

import leakcanary.AppWatcher;

public abstract class BaseFragment<T extends BaseViewModel, B extends ViewDataBinding> extends AlfonzBindingFragment<T, B> {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setupObservers();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// leak canary watcher
		AppWatcher.INSTANCE.getObjectWatcher().watch(this, "Watch Fragment");
		if (getActivity().isFinishing())
			AppWatcher.INSTANCE.getObjectWatcher().watch(getViewModel(), "Watch ViewModel");
	}

	public void showToast(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

	public void showSnackbar(String message) {
		if (getView() != null) {
			Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
		}
	}

	private void setupObservers() {
		getViewModel().observeEvent(this, ToastEvent.class, toastEvent -> showToast(toastEvent.message));
		getViewModel().observeEvent(this, SnackbarEvent.class, snackbarEvent -> showSnackbar(snackbarEvent.message));
	}
}
