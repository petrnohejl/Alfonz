package org.alfonz.samples.alfonzadapter;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import org.alfonz.samples.R;
import org.alfonz.samples.alfonzarch.BaseFragment;
import org.alfonz.samples.databinding.FragmentAdapterSampleGridBinding;

public class AdapterSampleGridSimpleFragment extends BaseFragment<AdapterSampleViewModel, FragmentAdapterSampleGridBinding> implements AdapterSampleView {
	private MessageListSimpleAdapter mAdapter;

	public static AdapterSampleGridSimpleFragment newInstance() {
		return new AdapterSampleGridSimpleFragment();
	}

	@Override
	public AdapterSampleViewModel setupViewModel() {
		AdapterSampleViewModel viewModel = new ViewModelProvider(this).get(AdapterSampleViewModel.class);
		getLifecycle().addObserver(viewModel);
		return viewModel;
	}

	@Override
	public FragmentAdapterSampleGridBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
		return FragmentAdapterSampleGridBinding.inflate(inflater);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getBinding().executePendingBindings(); // helps to reload recycler scroll position after orientation change
		setupAdapter();
	}

	@Override
	public void onItemClick(String message) {
		String newMessage = getViewModel().addMessage();
		showSnackbar(getString(R.string.adapter_sample_hello, newMessage));
	}

	@Override
	public boolean onItemLongClick(String message) {
		getViewModel().removeMessage(message);
		showToast(getString(R.string.adapter_sample_goodbye, message));
		return true;
	}

	private void setupAdapter() {
		if (mAdapter == null) {
			mAdapter = new MessageListSimpleAdapter(this, getViewModel());
			getBinding().adapterSampleGridRecycler.setAdapter(mAdapter);
		}
	}
}
