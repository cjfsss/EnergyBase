package hos.base.activity;

import android.content.Intent;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import hos.base.permission.IPermissionUse;
import hos.base.permission.Permission;
import hos.base.permission.PermissionSource;

/**
 * <p>Title: PermissionActivity </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/6 20:19
 */
public abstract class PermissionActivity extends BaseActivity implements IPermissionUse {

    @Nullable
    private Permission mPermission;

    public PermissionActivity() {
    }

    //    @ContentView
    public PermissionActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @NonNull
    protected Permission getPermissions(){
        if (mPermission == null) {
            mPermission = new PermissionSource(this);
        }
        return mPermission;
    }

    @Override
    public void requestPermission(@NonNull List<String> permissions) {
        if (mPermission == null) {
            mPermission = getPermissions();
        }
        mPermission.requestPermission(permissions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPermission != null) {
            mPermission.onActivityResult(requestCode);
        }
    }
}
