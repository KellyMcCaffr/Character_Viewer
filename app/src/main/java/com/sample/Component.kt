package com.sample

import com.sample.view.MainActivity
import dagger.Component

@Component
interface Component {
    fun injectMainActivity(activity: MainActivity)
}