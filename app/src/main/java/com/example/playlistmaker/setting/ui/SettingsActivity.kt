package com.example.playlistmaker.setting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySetingsBinding


class SettingsActivity() :  AppCompatActivity() {

    private lateinit var viewModel: SettingViewModel
    private lateinit var binding: ActivitySetingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySetingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,
            SettingViewModel.getViewModelFactory(" "))[SettingViewModel::class.java]

       // изменение темы приложения
        binding.themeSwitcher.isChecked = viewModel.getThemeState()

        viewModel.theme.observe(this) { checked ->
            binding.themeSwitcher.isChecked = checked
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)}


        // НАЗАД
        binding.backOff.setOnClickListener {
            finish()
        }

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
}