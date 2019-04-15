package com.cjc.luaapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lua.*
import org.keplerproject.luajava.LuaStateFactory



class LuaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lua)

        val lua = LuaStateFactory.newLuaState() //创建栈
        lua.openLibs() //加载标准库

        // lua 脚本： 函数
        val l = """
function test(a, b)
    return a + b, a - b;
end
        """.trimIndent();

        // 加载 lua 脚本
        lua.LdoString(l);

        // 获取全局变量或全局函数，入栈
        lua.getGlobal("test")

        // 把函数到参数压入栈
        lua.pushInteger(2)
        lua.pushInteger(3)

        // 调用函数
        lua.pcall(2, 2, 0)

        // 获取返回值
        val r1 = lua.toInteger(-2)
        val r2 = lua.toInteger(-1)
        Toast.makeText(this, "$r1, $r2", Toast.LENGTH_LONG).show()


        // lua 脚本： 函数
        val script = """
function test()
    tv:setText("hello from lua: "..str);
end
        """.trimIndent();

        // 加载 lua 脚本
        lua.LdoString(script);

        // 设置全局变量
        lua.pushString("hi")  // 变量值
        lua.setGlobal("str")      // 变量名

        lua.pushJavaObject(tv)    // 变量值
        lua.setGlobal("tv")       // 变量名

        // 获取全局变量或全局函数，入栈
        lua.getGlobal("test")

        // 调用函数
        lua.pcall(0, 0, 0)

        lua.close() // 在执行完毕后销毁Lua栈。
    }
}
