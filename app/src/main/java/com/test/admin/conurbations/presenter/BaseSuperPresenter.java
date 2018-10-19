package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.retrofit.AppClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class BasePresenter<V> {
    public V mvpView;
    protected GankService apiStores;
    private CompositeDisposable compositeDisposable;

    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        apiStores = AppClient.retrofit().create(GankService.class);
    }


    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public void addSubscription(Observable observable, DisposableObserver disposableObserver) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add((Disposable) observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver));
    }
}
