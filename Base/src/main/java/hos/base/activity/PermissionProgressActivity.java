package hos.base.activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import hos.base.view.IViewLoading;
import hos.util.dialog.DialogInterface;
import hos.utilx.dialog.ProgressDialog;


/**
 * <p>Title: PermissionProgressActivity </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/6 20:50
 */
public abstract class PermissionProgressActivity extends PermissionActivity implements IViewLoading {

    @Nullable
    private DialogInterface<?> mProgressDialog;

    public PermissionProgressActivity() {
    }

//    @ContentView
    public PermissionProgressActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @NonNull
    protected DialogInterface<?> getProgressDialog(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        return mProgressDialog;
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }

    @Override
    public void showLoading(@StringRes int titleId) {
        showLoading(getResources().getString(titleId));
    }

    @Override
    public void showLoading(@NonNull String title, boolean isDismissOnBackPressed,
                            boolean isDismissOnTouchOutside) {
        if (mProgressDialog == null) {
            mProgressDialog = getProgressDialog();
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setCancelable(isDismissOnBackPressed);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        postOnMain(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }
}
