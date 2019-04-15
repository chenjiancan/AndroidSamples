package com.cjc.toucheventexam

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


/*
总结：

三个关键方法: dispatchTouchEvent, onInterceptTouchEvent, onTouchEvent

dispatchTouchEvent: 用于决定是否继续派发 touchEvent

onInterceptTouchEvent: ViewGroup 用于拦截事件向子控件传递，拦截后自己 onTouchEvent() 处理，不再向下传递, 子控件接收不到了

onTouchEvent: 用于接收事件，并决定是否处理，返回 true 则表示该控件处理了该事件，不会再调用其他控件的 onTouchEvent()

在一个控件内，执行可以理解为是：
fun dispatchTouchEvent(ev) {

    if(onInterceptTouchEvent(ev)) {
        return true
    }

    for (child in children) {
        if(!child.dispatchTouchEvent(ev)) {
            break
        }
        return !child.onTouchEvent()

    }
    return true
}

if(dispatchTouchEvent(ev)) {
    return onTouchEvent(ev)
}

 */

class LinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("LinearLayout", "dispatchTouchEvent $event")
        return super.dispatchTouchEvent(event)

//        Log.e("LinearLayout", "不派发事件 $event")
//        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        Log.e("LinearLayout", "onInterceptTouchEvent $event")
        return super.onInterceptTouchEvent(event)

//        Log.e("LinearLayout", "拦截事件 $event")
//        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("LinearLayout", "onTouchEvent $event")
//return true
        return super.onTouchEvent(event)
    }
}

class TextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TextView", "dispatchTouchEvent $event")

        return super.dispatchTouchEvent(event) }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TextView", "onTouchEvent $event")
        return super.onTouchEvent(event)
    }
}


class Button @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("Button", "dispatchTouchEvent $event")
        return super.dispatchTouchEvent(event)
//        Log.e("Button", "button 不派发 $event")
//        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("Button", "onTouchEvent $event")

        return super.onTouchEvent(event)
//        Log.e("Button", "consume $event")
//        return true
    }
}



class TouchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)
        
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TouchActivity", "dispatchTouchEvent $event")
        // 返回 false， 阻止继续派发事件，所有控件包括本身都不会接收到
        return super.dispatchTouchEvent(event)
//        return false
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TouchActivity", "onTouchEvent $event")

        return super.onTouchEvent(event)
    }
}

/* button 消费掉事件
com.cjc.toucheventexam E/TouchActivity: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=16.0, y[0]=173.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997315, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=16.0, y[0]=39.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997315, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: onInterceptTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=16.0, y[0]=39.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997315, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=16.0, y[0]=10.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997315, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: onTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=16.0, y[0]=10.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997315, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: consume MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=16.0, y[0]=10.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997315, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/TouchActivity: dispatchTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=16.0, y[0]=173.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997466, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: dispatchTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=16.0, y[0]=39.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997466, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: onInterceptTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=16.0, y[0]=39.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997466, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: dispatchTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=16.0, y[0]=10.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997466, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: onTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=16.0, y[0]=10.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997466, downTime=11997315, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: consume MotionEvent { action=ACTION_UP, id[0]=0, x[0]=16.0, y[0]=10.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=11997466, downTime=11997315, deviceId=0, source=0x1002 }
 */

/* button 不派发事件，自己就不处理
com.cjc.toucheventexam E/TouchActivity: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=1.0, y[0]=183.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238740, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=1.0, y[0]=49.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238740, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: onInterceptTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=1.0, y[0]=49.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238740, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=1.0, y[0]=20.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238740, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/Button: button 不派发 MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=1.0, y[0]=20.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238740, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: onTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=1.0, y[0]=49.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238740, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/TouchActivity: onTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=1.0, y[0]=183.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238740, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/TouchActivity: dispatchTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=1.0, y[0]=183.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238893, downTime=12238740, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/TouchActivity: onTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=1.0, y[0]=183.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12238893, downTime=12238740, deviceId=0, source=0x1002 }

 */

/* viewGroup 拦截事件，事件由自身 onTouchEvent 处理，终止向子控件传递
com.cjc.toucheventexam E/TouchActivity: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=18.0, y[0]=180.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556444, downTime=12556444, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: dispatchTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=18.0, y[0]=46.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556444, downTime=12556444, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: onInterceptTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=18.0, y[0]=46.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556444, downTime=12556444, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: 拦截事件 MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=18.0, y[0]=46.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556444, downTime=12556444, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/LinearLayout: onTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=18.0, y[0]=46.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556444, downTime=12556444, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/TouchActivity: onTouchEvent MotionEvent { action=ACTION_DOWN, id[0]=0, x[0]=18.0, y[0]=180.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556444, downTime=12556444, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/TouchActivity: dispatchTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=18.0, y[0]=180.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556596, downTime=12556444, deviceId=0, source=0x1002 }
com.cjc.toucheventexam E/TouchActivity: onTouchEvent MotionEvent { action=ACTION_UP, id[0]=0, x[0]=18.0, y[0]=180.0, toolType[0]=TOOL_TYPE_FINGER, buttonState=BUTTON_PRIMARY|BUTTON_FORWARD, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=12556596, downTime=12556444, deviceId=0, source=0x1002 }

 */