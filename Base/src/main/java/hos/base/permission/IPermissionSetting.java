package hos.base.permission;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * <p>Title: IPermission </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/2 13:35
 */
public interface IPermissionSetting {

    /**
     * 跳转到权限设置界面
     *
     * @param permissions
     */
    void gotoSettingPermission(List<String> permissions);

    /**
     * 取消跳转到权限设置界面
     */
    void cancelGotoSettingPermission();

    /**
     * 设置界面返回
     *
     * @param permissions 所有的权限
     */
    void onActivitySettingPermissionResult(@NonNull List<String> permissions);

}
