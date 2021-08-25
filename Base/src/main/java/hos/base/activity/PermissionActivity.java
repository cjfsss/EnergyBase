package hos.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hos.base.permission.IPermission;
import hos.base.permission.IPermissionSetting;
import hos.base.permission.PermissionUtils;

/**
 * <p>Title: PermissionActivity </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/6 20:19
 */
public abstract class PermissionActivity extends BaseActivity implements IPermission, IPermissionSetting {

    @Nullable
    private List<String> mPermissions;

    public PermissionActivity() {
    }

    //    @ContentView
    public PermissionActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @NonNull
    private List<String> getPermissions() {
        if (mPermissions == null) {
            return mPermissions = new ArrayList<>();
        }
        return mPermissions;
    }

    @Override
    public void requestPermission(@NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onPermissionSuccess();
            return;
        }

        int permissionSize = permissions.size();
        String[] permissionArray = permissions.toArray(new String[permissionSize]);
        // 获取需要申请的权限
        List<String> needPermissionList = PermissionUtils.getNeedPermissionList(this, permissionArray);
        if (needPermissionList.size() == 0) {
            // 没有需要申请的权限，不需要再进行申请了
            onPermissionSuccess();
            return;
        }
        int size = needPermissionList.size();
        if (size == 1) {
            // 只有一个权限
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        // 权限申请成功
                        onPermissionSuccess();
                    } else {
                        onPermissionError(permissions, needPermissionList);
                    }
                }
            }).launch(needPermissionList.get(0));
        }
        // 有多个权限
        registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                List<String> needPermissionList = PermissionUtils.getNeedPermissionList(result);
                if (needPermissionList.size() == 0) {
                    // 权限申请成功
                    onPermissionSuccess();
                } else {
                    onPermissionError(permissions, needPermissionList);
                }
            }
        }).launch(needPermissionList.toArray(new String[size]));
    }

    @Override
    public void onPermissionError(@NonNull List<String> permissions, @NonNull List<String> errorPermissions) {
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setCancelable(false)
                .setMessage("权限申请失败，请允许我们进行设置")
                .setNegativeButton(
                        "申请权限",
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                gotoSettingPermission(permissions);
                            }
                        })
                .setPositiveButton("取消申请",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 取消
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                cancelGotoSettingPermission();
                            }
                        }).show();
    }

    @Override
    public void gotoSettingPermission(@NonNull List<String> permissions) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onActivitySettingPermissionResult(permissions);
            }
        }).launch(intent);
    }

    @Override
    public void onActivitySettingPermissionResult(@NonNull List<String> permissions) {
        // 重新申请权限
        requestPermission(permissions);
    }

}
