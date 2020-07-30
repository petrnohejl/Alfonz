package org.alfonz.rx.utility;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public final class SchedulersUtility {
	private static ObservableTransformer<?, ?> sSchedulersObservableTransformer;
	private static SingleTransformer<?, ?> sSchedulersSingleTransformer;
	private static CompletableTransformer sSchedulersCompletableTransformer;
	private static MaybeTransformer<?, ?> sSchedulersMaybeTransformer;
	private static FlowableTransformer<?, ?> sSchedulersFlowableTransformer;

	private SchedulersUtility() {}

	@NonNull
	@SuppressWarnings("unchecked")
	public static <T> ObservableTransformer<T, T> applyObservableSchedulers() {
		if (sSchedulersObservableTransformer == null) {
			sSchedulersObservableTransformer = observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return (ObservableTransformer<T, T>) sSchedulersObservableTransformer;
	}

	@NonNull
	@SuppressWarnings("unchecked")
	public static <T> SingleTransformer<T, T> applySingleSchedulers() {
		if (sSchedulersSingleTransformer == null) {
			sSchedulersSingleTransformer = single -> single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return (SingleTransformer<T, T>) sSchedulersSingleTransformer;
	}

	@NonNull
	public static CompletableTransformer applyCompletableSchedulers() {
		if (sSchedulersCompletableTransformer == null) {
			sSchedulersCompletableTransformer = completable -> completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return sSchedulersCompletableTransformer;
	}

	@NonNull
	@SuppressWarnings("unchecked")
	public static <T> MaybeTransformer<T, T> applyMaybeSchedulers() {
		if (sSchedulersMaybeTransformer == null) {
			sSchedulersMaybeTransformer = maybe -> maybe.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return (MaybeTransformer<T, T>) sSchedulersMaybeTransformer;
	}

	@NonNull
	@SuppressWarnings("unchecked")
	public static <T> FlowableTransformer<T, T> applyFlowableSchedulers() {
		if (sSchedulersFlowableTransformer == null) {
			sSchedulersFlowableTransformer = flowable -> flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		}
		return (FlowableTransformer<T, T>) sSchedulersFlowableTransformer;
	}
}
