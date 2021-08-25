package hos.base.view;


import android.annotation.SuppressLint;

import androidx.arch.core.executor.ArchTaskExecutor;

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

    @SuppressLint("RestrictedApi")
    default void postIo(Runnable runnable) {
        ArchTaskExecutor.getInstance().executeOnDiskIO(runnable);
    }

    @SuppressLint("RestrictedApi")
    default void postToUI(Runnable runnable) {
        ArchTaskExecutor.getInstance().postToMainThread(runnable);
    }

    @SuppressLint("RestrictedApi")
    default void postOnUI(Runnable runnable) {
        ArchTaskExecutor.getInstance().executeOnMainThread(runnable);
    }
}
