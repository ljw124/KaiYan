package com.dcdz.kaiyanforkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.apache.log4j.Logger

class MainActivity : AppCompatActivity() {

    //internal 修饰符，模块内可见
    internal var log = Logger.getLogger(MainActivity::class.java!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
