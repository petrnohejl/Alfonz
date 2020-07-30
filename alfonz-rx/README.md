Alfonz - Rx Module
==================

Helper classes for managing RxJava observables and subscriptions.

The purpose of this module is to simplify work with RxJava. This module provides helpers and utilities for registering and managing Disposables, keeping info about currently running RxJava calls and its types, setting up Observables and Schedulers, working with reactive event bus. Specialized reactive base types `Single`, `Completable` and `Maybe` are also supported.


How to use RxManager
--------------------

Create a new instance of `RxManager` in your ViewModel.

```java
private RxManager mRxManager = new RxManager();
```

Create a new instance of `Observer`.

```java
private DisposableObserver<String> createHelloObserver() {
	return AlfonzDisposableObserver.newInstance(
			data ->
			{
				// onNext
			},
			throwable ->
			{
				// onError
			},
			() ->
			{
				// onComplete
			}
	);
}
```

Run RxJava call. In the following example, we will check if a call of the specific type is already running. If not, we will execute it, otherwise we will do nothing. We will create an `Observable`, set it up with Schedulers and subscribe with the `Observer`. The call is automatically registered on subscribe so we can check which specific calls are currently running. After the RxJava task terminates, the call is automatically unregistered. Disposables are automatically stored in `CompositeDisposable` container. Schedulers should be applied at the end of the stream, right before the `subscribeWith()` is called.

```java
private static final String HELLO_CALL_TYPE = "hello";

private void runHelloCall() {
	if (!mRxManager.isRunning(HELLO_CALL_TYPE)) {
		Observable<String> rawObservable = Observable.just("Hello").map(s -> s + " world!");
		Observable<String> observable = mRxManager.setupObservableWithSchedulers(rawObservable, HELLO_CALL_TYPE);
		observable.subscribeWith(createHelloObserver());
	}
}
```

Don't forget to dispose all the Disposables when you don't need them anymore.

```java
@Override
public void onDestroy() {
	super.onDestroy();
	mRxManager.disposeAll();
}
```

If you would call `isRunning()` right after `runHelloCall()` in the previous example, it might return false. It's because the call is considered as running after you actually subscribe which could happen a few milliseconds after calling the `isRunning()`. Keep in mind that you are usually subscribing on another thread. Use `isPending()` to find if there is any call pending. Use `!mRxManager.isPending() && !mRxManager.isRunning()` to make sure that a specific call has not been executed yet.


How to use RxBus
----------------

Register `RxBus` event bus in your ViewModel and listen to specific events.

```java
RxBus.getInstance()
		.onEvent(Long.class)
		.doOnSubscribe(mCompositeDisposable::add)
		.subscribe(event -> Logcat.d("received " + event.toString()));
```

Send events from any part of your app.

```java
RxBus.getInstance().send(Long.valueOf(System.currentTimeMillis()));
```

Don't forget to dispose all the Disposables when you don't need them anymore.

```java
@Override
public void onDestroy() {
	super.onDestroy();
	mCompositeDisposable.clear();
}
```


Dependencies
------------

* AndroidX
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxRelay](https://github.com/JakeWharton/RxRelay)


Samples and download
--------------------

See the main [README](https://github.com/petrnohejl/Alfonz/) file.
