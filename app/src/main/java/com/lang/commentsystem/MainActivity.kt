package com.lang.commentsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.add
import androidx.fragment.app.replace
import com.facebook.drawee.view.SimpleDraweeView
import com.lang.commentsystem.epoxy.EpoxyFragment
import com.lang.commentsystem.groupie.GroupieFragment
import com.lang.commentsystem.utils.loadImageUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.epoxy).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add<EpoxyFragment>(R.id.fragmentContainerView)
                .commitNow()
        }

        findViewById<Button>(R.id.groupie).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add<GroupieFragment>(R.id.fragmentContainerView)
                .commitNow()
        }
    }
}