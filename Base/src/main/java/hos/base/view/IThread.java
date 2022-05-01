package hos.base.view;


import hos.thread.executor.TS;
import hos.thread.executor.ThreadTaskExecutor;
import hos.thread.hander.MH;

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
        TS.postIo(runnable);
    }

    default void postOnIo(Runnable runnable) {
        TS.postOnIo(runnable);
    }

    default void postToMain(Runnable runnable) {
        MH.postToMain(runnable);
    }

    default void postOnMain(Runnable runnable) {
        MH.postOnMain(runnable);
    }
}
