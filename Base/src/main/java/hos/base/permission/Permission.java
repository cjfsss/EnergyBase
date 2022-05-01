package hos.base.permission;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

import hos.util.dialog.TitleDialogInterface;
import hos.util.listener.OnTargetListener;

/**
 * <p>Title: PermissionImpl </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2022/5/1 18:50
 */
public abstract class Permission implements IPermission, IPermissionSetting {

    @Nullable
    protected Fragment fragment;
    @Nullable
    protected AppCompatActivity activity;
    @Nullable
    protected List<String> permissionList;
    @Nullable
    protected List<String> needPermissionList;
    @Nullable
    protected ActivityResultLauncher<Intent> settingPermissionLauncher;

    public Permission(@NonNull Fragment fragment) {
        this.fragment = fragment;
        initRegister(activity, fragment);
        fragment.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    // 销毁的时候执行
                    source.getLifecycle().removeObserver(this);
                    onDestroy();
                }
            }
        });
    }


    public Permission(@NonNull AppCompatActivity activity) {
        this.activity = activity;
        initRegister(activity, fragment);
        activity.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    // 销毁的时候执行
                    source.getLifecycle().removeObserver(this);
                    onDestroy();
                }
            }
        });
    }

    protected void initRegister(@Nullable AppCompatActivity activity, @Nullable Fragment fragment) {
        if (activity != null) {
            settingPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    onActivitySettingPermissionResult(getPermissions());
                }
            });
        } else if (fragment != null) {
            settingPermissionLauncher = fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    onActivitySettingPermissionResult(getPermissions());
                }
            });
        }
    }

    @NonNull
    public List<String> getPermissions() {
        if (permissionList == null) {
            return permissionList = new ArrayList<>();
        }
        return permissionList;
    }


    @Override
    public void requestPermission(@NonNull List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            onPermissionSuccess();
            return;
        }
        if (getPermissions().isEmpty()) {
            getPermissions().addAll(permissions);
        }
        int permissionSize = permissions.size();
        String[] permissionArray = permissions.toArray(new String[permissionSize]);
        // 获取需要申请的权限
        FragmentActivity _activity = getActivity();
        if (_activity == null) {
            onPermissionSuccess();
            return;
        }
        needPermissionList = PermissionUtils.getNeedPermissionList(_activity, permissionArray);
        if (needPermissionList.size() == 0) {
            // 没有需要申请的权限，不需要再进行申请了
            onPermissionSuccess();
            return;
        }
        requestPermissionReal(needPermissionList);
    }

    protected abstract void requestPermissionReal(List<String> needPermissionList);

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void onPermissionError(@NonNull List<String> permissions) {
        TitleDialogInterface<?> errorDialog = getErrorDialog();
        if (errorDialog == null) {
            cancelGotoSettingPermission();
            return;
        }
        errorDialog.setTitle("温馨提示")
                .setContent("权限申请失败，请允许我们进行设置");
        errorDialog.setCancel((OnTargetListener) o -> {
            if (o instanceof TitleDialogInterface) {
                ((TitleDialogInterface) o).dismiss();
            }
            cancelGotoSettingPermission();
        }).setConfirm(o -> {
            if (o instanceof TitleDialogInterface) {
                ((TitleDialogInterface) o).dismiss();
            }
            gotoSettingPermission(permissions);
        }).setCancelable(false);
        errorDialog.show();
    }

    public abstract TitleDialogInterface<?> getErrorDialog();

    @Nullable
    public FragmentActivity getActivity() {
        FragmentActivity _activity = activity;
        if (activity == null && fragment != null) {
            _activity = fragment.requireActivity();
        }
        return _activity;
    }

    @Override
    public void gotoSettingPermission(List<String> permissions) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        FragmentActivity _activity = getActivity();
        if (_activity == null) {
            onPermissionSuccess();
            return;
        }
        intent.setData(Uri.parse("package:" + _activity.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (settingPermissionLauncher != null) {
            settingPermissionLauncher.launch(intent);
        } else {
            if (activity != null) {
                activity.startActivityForResult(intent, 1000);
            } else if (fragment != null) {
                fragment.startActivityForResult(intent, 1000);
            }
        }
    }

    public void onActivityResult(int requestCode) {
        if (requestCode == 1000) {
            onActivitySettingPermissionResult(getPermissions());
        }
    }


    @Override
    public void onActivitySettingPermissionResult(@NonNull List<String> permissions) {
        // 重新申请权限
        requestPermission(permissions);
    }

    @Override
    public void onPermissionSuccess() {
        if (activity instanceof IPermissionUse) {
            ((IPermissionUse) activity).onPermissionSuccess();
        } else if (fragment instanceof IPermissionUse) {
            ((IPermissionUse) fragment).onPermissionSuccess();
        }
    }

    @Override
    public void cancelGotoSettingPermission() {
        if (activity instanceof IPermissionUse) {
            ((IPermissionUse) activity).cancelPermission();
        } else if (fragment instanceof IPermissionUse) {
            ((IPermissionUse) fragment).cancelPermission();
        }
    }

    protected void onDestroy() {
        if (permissionList != null) {
            permissionList.clear();
        }
        if (needPermissionList != null) {
            needPermissionList.clear();
        }
        fragment = null;
        activity = null;
        settingPermissionLauncher = null;
    }
}
