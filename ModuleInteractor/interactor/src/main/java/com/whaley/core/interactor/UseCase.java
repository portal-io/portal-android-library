package com.whaley.core.interactor;

import com.google.common.base.Preconditions;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 业务用例抽象类
 * 代表唯一的单个细分业务
 * Created by YangZhi on 2017/6/28 15:58.
 */
public abstract class UseCase<T,Params> {

    private Scheduler executeThread;
    private Scheduler postExecutionThread;
    private final CompositeDisposable disposables;
    private IRepositoryManager repositoryManager;


    public UseCase(){
        this(null, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    /**
     * 构造 UseCase 需要传递执行调度线程 和 回调线程
     * @param repositoryManager 仓库管理对象
     * @param executeThread 执行任务线程
     * @param postExecutionThread 回调线程
     */
    public UseCase(IRepositoryManager repositoryManager,Scheduler executeThread, Scheduler postExecutionThread) {
        this.repositoryManager = repositoryManager;
        this.executeThread = executeThread;
        this.postExecutionThread = postExecutionThread;
        this.disposables = new CompositeDisposable();
    }

    public void setRepositoryManager(IRepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public void setExecuteThread(Scheduler executeThread) {
        this.executeThread = executeThread;
    }

    public void setPostExecutionThread(Scheduler postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    /**
     * 构造 UseCase 的 Observable 对象
     * @param params 参数 如不需要则传 null
     * @return 该 UseCase 的 Observable 对象
     */
    public abstract Observable<T> buildUseCaseObservable(Params params);

    /**
     * 执行 UseCase
     * @param observer 执行该 UseCase 的观察者
     */
    public void execute(DisposableObserver<T> observer) {
        execute(observer,null);
    }

    /**
     * 执行 UseCase
     * @param observer 执行该 UseCase 的观察者
     * @param params 构造 UseCase Observable 对象的形参
     */
    public void execute(DisposableObserver<T> observer, Params params) {
        final Observable<T> observable = this.buildUseCaseObservable(params);
        execute(observable,observer);
    }

    /**
     * 执行 UseCase
     * @param observable 执行的被观察者
     * @param observer 执行该 UseCase 的观察者
     */
    public void execute(Observable<T> observable,DisposableObserver<T> observer) {
        Preconditions.checkNotNull(observer);
        observable=observable
                .subscribeOn(executeThread)
                .observeOn(postExecutionThread);
        addDisposable(observable.subscribeWith(observer));
    }

    /**
     * 打断该 UseCase
     */
    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * 将 UseCase 加入到可打断容器中
     * @param disposable
     */
    private void addDisposable(Disposable disposable) {
        Preconditions.checkNotNull(disposable);
        Preconditions.checkNotNull(disposables);
        disposables.add(disposable);
    }

    /**
     * 获得数据仓储管理对象
     * @return
     */
    public IRepositoryManager getRepositoryManager() {
        return repositoryManager;
    }
}
