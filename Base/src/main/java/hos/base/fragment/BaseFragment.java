package hos.base.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import hos.base.activity.BaseActivity;
import hos.base.view.IFragmentActivity;
import hos.base.view.IThread;
import hos.core.ActivityManager;

/**
 * <p>Title: BaseFragment1 </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/6 20:52
 */
public abstract class BaseFragment extends Fragment implements IFragmentActivity<BaseActivity>, IThread {

    public BaseFragment() {
    }

    public BaseFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewShowed(view);
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        // 解决fragment有动画切换卡顿问题
        //如何nextAnim是-1或0那么就是没有设置转场动画，直接走super就行了
        if (nextAnim <= 0) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        if (enter) {
            Animation animation = loadAnimation(activity(), nextAnim);
            if (animation == null) {
                return super.onCreateAnimation(transit, enter, nextAnim);
            }
            //延迟100毫秒执行让View有一个初始化的时间，防止初始化时刷新页面与动画刷新冲突造成卡顿
            animation.setStartOffset(getStartDelay());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onEnterAnimEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            return animation;
        }
        Animation animation = loadAnimation(activity(), nextAnim);
        if (animation == null) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        //延迟100毫秒执行让View有一个初始化的时间，防止初始化时刷新页面与动画刷新冲突造成卡顿
        animation.setStartOffset(getStartDelay());
        return animation;
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        // 解决fragment有动画切换卡顿问题
        //如何nextAnim是-1或0那么就是没有设置转场动画，直接走super就行了
        if (nextAnim <= 0) {
            return super.onCreateAnimator(transit, enter, nextAnim);
        }
        if (enter) {
            Animator animation = loadAnimator(activity(), nextAnim);
            if (animation == null) {
                return super.onCreateAnimator(transit, enter, nextAnim);
            }
            //延迟100毫秒执行让View有一个初始化的时间，防止初始化时刷新页面与动画刷新冲突造成卡顿
            animation.setStartDelay(getStartDelay());
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onEnterAnimEnd();
                }
            });
            return animation;
        }
        Animator animation = loadAnimator(activity(), nextAnim);
        if (animation == null) {
            return super.onCreateAnimator(transit, enter, nextAnim);
        }
        //延迟100毫秒执行让View有一个初始化的时间，防止初始化时刷新页面与动画刷新冲突造成卡顿
        animation.setStartDelay(getStartDelay());
        return animation;
    }

    @Nullable
    protected Animation loadAnimation(@NonNull BaseActivity activity, int nextAnim) {
        return null;
    }

    @Nullable
    protected Animator loadAnimator(@NonNull BaseActivity activity, int nextAnim) {
        return null;
    }

    protected long getStartDelay() {
        return 0;
    }

    /**
     * 如果真的要延迟初始化，那么重写这个方法，等动画结束了再初始化
     */
    protected void onEnterAnimEnd() {
    }

    /**
     * 获取Bundle
     *
     * @return 返回Intent中的Bundle
     */
    @Nullable
    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    @NonNull
    @Override
    public BaseActivity activity() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity instanceof BaseActivity) {
                return (BaseActivity) activity;
            }
        }
        Activity currentActivity = ActivityManager.getInstance().getTopActivity();
        if (currentActivity instanceof BaseActivity) {
            return (BaseActivity) currentActivity;
        }
        return (BaseActivity) requireActivity();
    }

    /**
     * 销毁当前 Fragment 所在的 Activity
     */
    @Override
    public void finish() {
        activity().finish();
    }

}
