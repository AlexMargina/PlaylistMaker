package com.example.playlistmaker.setting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySetingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity() :  AppCompatActivity() {

    private lateinit var binding: ActivitySetingsBinding
    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.theme.observe(this) {
                checked -> binding.themeSwitcher.isChecked = checked   }

        binding = ActivitySetingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // НАЗАД
        binding.backOff.setOnClickListener {
            finish()
        }

        // слежение и изменение темы приложения
        binding.themeSwitcher.isChecked = viewModel.getThemeState()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked) }

        // ПОДЕЛИТЬСЯ
        binding.imageButtonShare.setOnClickListener {
            viewModel.shareApp()
        }

        //НАПИСАТЬ в ПОДДЕРЖКУ
        binding.imageButtonSupport.setOnClickListener {
            viewModel.writeInSupport()
        }

        //ПОЛЬЗОВАТЕЛЬСКОЕ СОГЛАШЕНИЕ
        binding.frameLayoutOfer.setOnClickListener {
            viewModel.openUserAgreement()
        }
    }
     fun changeTheme (checked : Boolean){
        viewModel.switchTheme(checked)
    }
}

