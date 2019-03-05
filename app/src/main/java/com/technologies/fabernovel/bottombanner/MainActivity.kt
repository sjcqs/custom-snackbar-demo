package com.technologies.fabernovel.bottombanner

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snackbar.setOnClickListener {
            Snackbar.make(container, "Ceci est une snackbar", Snackbar.LENGTH_SHORT).show()
        }
        bottomBar.setOnClickListener {
            BottomBanner.make(container, "Ceci est une snackbar custom", BottomBanner.LENGTH_SHORT)
                .setIcon(R.drawable.ic_thumb_up)
                .setAction("Annuler") {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        largebanner.setOnClickListener {
            LargeBanner.make(container, "Ceci est une snackbar LARGE", LargeBanner.LENGTH_SHORT)
                .show()
        }
    }
}
