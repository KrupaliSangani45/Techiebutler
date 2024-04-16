package io.lipl.techiebutler.ui

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.lipl.techiebutler.domain.repository.RetrofitRepository
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    val activity: BaseActivity
        get() = this

}