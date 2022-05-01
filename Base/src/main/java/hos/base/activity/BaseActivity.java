package hos.base.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hos.base.view.IFragmentActivity;
import hos.base.view.IThread;

/**
 * <p>Title: BaseActivity </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/8/21 19:07
 */
public abstract class BaseActivity extends AppCompatActivity implements IThread, IFragmentActivity<BaseActivity> {

    public BaseActivity() {
    }

//    @ContentView
    public BaseActivity(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                onViewShowed(getWindow().getDecorView());
            }
        });
    }

    /**
     * 获取Bundle
     *
     * @return 返回Intent中的Bundle
     */
    @Nullable
    @Override
    public Bundle getBundle() {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        return intent.getBundleExtra("bundle");
    }

    @NonNull
    @Override
    public BaseActivity activity() {
        return this;
    }


}
