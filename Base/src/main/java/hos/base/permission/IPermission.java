package hos.base.permission;

import androidx.annotation.NonNull;

import java.util.Arrays;
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
public interface IPermission {
    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限
     */
    default void requestPermission(@NonNull String... permissions) {
        requestPermission(Arrays.asList(permissions));
    }

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限
     */
    void requestPermission(@NonNull List<String> permissions);

    /**
     * 权限申请成功
     */
    void onPermissionSuccess();

    /**
     * 申请权限失败
     *
     * @param permissions      所有的权限
     * @param errorPermissions 申请失败的权限
     */
    void onPermissionError(@NonNull List<String> permissions, @NonNull List<String> errorPermissions);

}
