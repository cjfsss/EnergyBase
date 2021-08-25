package hos.base.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <p>Title: IFragmentActivity </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/6 20:02
 */
public interface IFragmentActivity<A extends AppCompatActivity> {

    /**
     * 获取Bundle
     *
     * @return 返回Intent中的Bundle
     */
    @Nullable
    Bundle getBundle();

    @NonNull
    A activity();

    void onViewShowed(@NonNull View view);

    void finish();
}
