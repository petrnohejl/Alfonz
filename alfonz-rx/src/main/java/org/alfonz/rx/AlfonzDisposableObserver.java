package org.alfonz.rx;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class AlfonzDisposableObserver<T> extends DisposableObserver<T> {
	@Nullable private Consumer<T> mOnNextAction;
	@Nullable private Consumer<Throwable> mOnErrorAction;
	@Nullable private Action mOnCompleteAction;

	private AlfonzDisposableObserver(@Nullable Consumer<T> onNextAction, @Nullable Consumer<Throwable> onErrorAction, @Nullable Action onCompleteAction) {
		mOnNextAction = onNextAction;
		mOnErrorAction = onErrorAction;
		mOnCompleteAction = onCompleteAction;
	}

	public static <T> AlfonzDisposableObserver<T> newInstance() {
		return newInstance(null, null, null);
	}

	public static <T> AlfonzDisposableObserver<T> newInstance(@Nullable Consumer<T> onNextAction) {
		return newInstance(onNextAction, null, null);
	}

	public static <T> AlfonzDisposableObserver<T> newInstance(@Nullable Consumer<T> onNextAction, @Nullable Consumer<Throwable> onErrorAction) {
		return newInstance(onNextAction, onErrorAction, null);
	}

	public static <T> AlfonzDisposableObserver<T> newInstance(@Nullable Consumer<T> onNextAction, @Nullable Consumer<Throwable> onErrorAction, @Nullable Action onCompleteAction) {
		return new AlfonzDisposableObserver<>(onNextAction, onErrorAction, onCompleteAction);
	}

	@Override
	public void onNext(T value) {
		if (mOnNextAction == null) return;
		try {
			mOnNextAction.accept(value);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	@Override
	public void onError(Throwable t) {
		if (mOnErrorAction == null) return;
		try {
			mOnErrorAction.accept(t);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	@Override
	public void onComplete() {
		if (mOnCompleteAction == null) return;
		try {
			mOnCompleteAction.run();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
}
