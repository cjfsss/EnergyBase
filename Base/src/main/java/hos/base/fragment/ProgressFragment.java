package hos.base.fragment;

import android.app.ProgressDialog;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import hos.base.view.IViewLoading;

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
    private ProgressDialog mProgressDialog;

    public ProgressFragment() {
    }

    public ProgressFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @NonNull
    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            return mProgressDialog = new ProgressDialog(requireActivity());
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
        ProgressDialog progressDialog = getProgressDialog();
        progressDialog.setTitle(title);
        progressDialog.setCanceledOnTouchOutside(isDismissOnTouchOutside);
        progressDialog.setCancelable(isDismissOnBackPressed);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
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
