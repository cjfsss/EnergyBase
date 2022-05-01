package hos.base.fragment;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import hos.base.view.IViewLoading;
import hos.util.dialog.DialogInterface;
import hos.utilx.dialog.ProgressDialog;

/**
 * <p>Title: ProgressActivity </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/6 20:14
 */
public abstract class ProgressFragment extends BaseFragment implements IViewLoading {

    @Nullable
    private DialogInterface<?> mProgressDialog;

    public ProgressFragment() {
    }

    public ProgressFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @NonNull
    protected DialogInterface<?> getProgressDialog(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog((AppCompatActivity) requireActivity());
        }
        return mProgressDialog;
    }

    @Override
    public void onDestroyView() {
        hideLoading();
        super.onDestroyView();
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
