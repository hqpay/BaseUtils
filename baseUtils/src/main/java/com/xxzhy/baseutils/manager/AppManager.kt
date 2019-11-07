package com.xxzhy.baseutils.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*

class AppManager {
    companion object{
        private var activityStack: Stack<Activity>? = null
        private var instance: AppManager? = null
        fun initAppManager(): AppManager {
            if (instance == null) {
                synchronized(AppManager::class.java) {
                    if (instance == null) {
                        instance = AppManager()
                        val var10000 = instance
                        activityStack = Stack()
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * 添加activity
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取最后一个activity
     */
    fun currentActivity(): Activity? {
        return try {
            activityStack!!.lastElement() as Activity
        } catch (var2: Exception) {
            null
        }
    }

    /**
     * 获取倒数第二个activity
     */
    fun preActivity(): Activity? {
        val index = activityStack!!.size - 2
        return if (index < 0) {
            null
        } else {
            activityStack!![index] as Activity
        }
    }

    /**
     * 结束当前的Activity
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement() as Activity
       finishActivity(activity)
    }

    /**
     * 结束指定activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 根据类名移除activity
     */
    fun finishActivity(cls: Class<*>) {
        try {
            val var2 = activityStack!!.iterator()
            while (var2.hasNext()) {
                val activity = var2.next() as Activity
                if (activity.javaClass == cls) {
                    this.finishActivity(activity)
                }
            }
        } catch (var4: Exception) {
        }
    }

    /**
     * 结束全部activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                (activityStack!![i] as Activity).finish()
            }
            ++i
        }
        activityStack!!.clear()
    }
    /**
     * 判断是否是传入的activity
     */
    fun isCheckActivity(cls: Class<*>): Boolean {
        val var2 = activityStack!!.iterator()
        var activity: Activity
        do {
            if (!var2.hasNext()) {
                return false
            }
            activity = var2.next() as Activity
        } while (activity.javaClass != cls)

        return true
    }
    /**
     * 从占中移除指定activity
     */
    fun removeActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity = null
        }
    }

    /**
     * 返回到指定activity
     */
    fun returnToActivity(cls: Class<*>) {
        while (activityStack!!.size != 0 && (activityStack!!.peek() as Activity).javaClass != cls) {
            this.finishActivity(activityStack!!.peek() as Activity)
        }
    }

    /**
     * 判断传入activity是否打开
     */
    fun isOpenActivity(cls: Class<*>): Boolean {
        if (activityStack != null) {
            var i = 0

            val size = activityStack!!.size
            while (i < size) {
                if (cls == (activityStack!!.peek() as Activity).javaClass) {
                    return true
                }
                ++i
            }
        }
        return false
    }

    fun AppExit(context: Context, isBackground: Boolean) {
        try {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
        } catch (var7: Exception) {
        } finally {
            if (!isBackground) {
                System.exit(0)
            }
        }
    }
}