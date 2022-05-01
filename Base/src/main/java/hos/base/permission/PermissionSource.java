package hos.base.permission;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.List;
import java.util.Map;

import hos.util.dialog.TitleDialogInterface;
import hos.utilx.dialog.TitleDialog;

/**
 * <p>Title: DefalutPermission </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2022/5/1 19:16
 */
public class PermissionSource extends Permission {

    private ActivityResultLauncher<String> permissionLauncher;

    private ActivityResultLauncher<String[]> permissionMultipleLauncher;

    public PermissionSource(@NonNull Fragment fragment) {
        super(fragment);
    }

    public PermissionSource(@NonNull AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected void initRegister(@Nullable AppCompatActivity activity, @Nullable Fragment fragment) {
        super.initRegister(activity, fragment);
        if (activity != null) {
            permissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        // 权限申请成功
                        onPermissionSuccess();
                    } else {
                        onPermissionError(getPermissions());
                    }
                }
            });
            // 有多个权限
            permissionMultipleLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    List<String> needPermissionList = PermissionUtils.getNeedPermissionList(result);
                    if (needPermissionList.size() == 0) {
                        // 权限申请成功
                        onPermissionSuccess();
                    } else {
                        onPermissionError(getPermissions());
                    }
                }
            });
        } else if (fragment != null) {
            permissionLauncher = fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        // 权限申请成功
                        onPermissionSuccess();
                    } else {
                        onPermissionError(getPermissions());
                    }
                }
            });
            // 有多个权限
            permissionMultipleLauncher = fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    List<String> needPermissionList = PermissionUtils.getNeedPermissionList(result);
                    if (needPermissionList.size() == 0) {
                        // 权限申请成功
                        onPermissionSuccess();
                    } else {
                        onPermissionError(getPermissions());
                    }
                }
            });
        } else {
            onPermissionSuccess();
        }
    }

    @Override
    protected void requestPermissionReal(List<String> needPermissionList) {
        if (needPermissionList == null || needPermissionList.size() == 0) {
            onPermissionSuccess();
            return;
        }
        int size = needPermissionList.size();
        if (size == 1) {
            permissionLauncher.launch(needPermissionList.get(0));
            return;
        }
        permissionMultipleLauncher.launch(needPermissionList.toArray(new String[size]));
    }

    @Nullable
    @Override
    public TitleDialogInterface<?> getErrorDialog() {
        FragmentActivity _activity = getActivity();
        if (_activity instanceof AppCompatActivity) {
            return new TitleDialog((AppCompatActivity) _activity);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        permissionLauncher = null;
        permissionMultipleLauncher = null;
    }
}
