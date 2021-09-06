package hos.base.view;


import hos.thread.executor.ThreadTaskExecutor;

/**
 * <p>Title: IThread </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/8/21 18:01
 */
public interface IThread {

    default void postIo(Runnable runnable) {
        ThreadTaskExecutor.getInstance().postIo(runnable);
    }

    default void postToMain(Runnable runnable) {
        ThreadTaskExecutor.getInstance().postToMain(runnable);
    }

    default void postOnMain(Runnable runnable) {
        ThreadTaskExecutor.getInstance().postOnMain(runnable);
    }
}
