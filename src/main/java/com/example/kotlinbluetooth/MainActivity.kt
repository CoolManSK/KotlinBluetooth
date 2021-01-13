package com.example.kotlinbluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    //bluetooth adapter
    lateinit var bAdapter:BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        val tvBtStatus: TextView =  findViewById(R.id.bluetoothStatusTv)
        if(bAdapter==null)
        {
            tvBtStatus.text = "Bluetooth is not available"
        }
        else
        {
            tvBtStatus.text = "Bluetooth is available"
        }

        val ivBluetooth : ImageView = findViewById(R.id.bluetoothIv)
        if (bAdapter.isEnabled)
        {
            ivBluetooth.setImageResource(R.drawable.ic_bluetooth_on)
        }
        else
        {
            ivBluetooth.setImageResource(R.drawable.ic_bluetooth_off)
        }

        val btnTurnOn : Button = findViewById(R.id.turnOnBtn)
        btnTurnOn.setOnClickListener {
            if (bAdapter.isEnabled) {
                Toast.makeText(this, "Already on", Toast.LENGTH_LONG).show()
            }
            else {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_ENABLE_BT)
            }
        }

        val btnTurnOff : Button = findViewById(R.id.turnOffBtn)
        btnTurnOff.setOnClickListener {
            if (!bAdapter.isEnabled) {
                Toast.makeText(this, "Already off", Toast.LENGTH_LONG).show()
            }
            else {
                bAdapter.disable()
                val ivBluetooth : ImageView = findViewById(R.id.bluetoothIv)
                ivBluetooth.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_LONG).show()
            }
        }

        val btnDiscoverable : Button = findViewById(R.id.discoverableBtn)
        btnDiscoverable.setOnClickListener {
            if (!bAdapter.isDiscovering) {
                Toast.makeText(this, "Making your device discoverable", Toast.LENGTH_LONG).show()
                var intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent, REQUEST_DISCOVERABLE_BT)
            }
        }

        val btnPaired : Button = findViewById(R.id.pairedBtn)
        btnPaired.setOnClickListener {
            if (bAdapter.isEnabled) {
                val tvPaired : TextView = findViewById(R.id.pairedTv)
                tvPaired.text = "Paired Devices"
                val devices = bAdapter.bondedDevices
                for (device in devices){
                    val deviceName = device.name
                    val deviceAddress = device
                    tvPaired.append("\nDevice: $deviceName, $device")
                }
            }
            else{
                Toast.makeText(this, "Turn on bluetooth first", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {
                    val ivBluetooth : ImageView = findViewById(R.id.bluetoothIv)
                    ivBluetooth.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth is on",  Toast.LENGTH_LONG).show()
                }
            else {
                    Toast.makeText(this, "Could not on bluetooth",  Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        private val REQUEST_ENABLE_BT = 1
        private val REQUEST_DISCOVERABLE_BT = 2
        // Stops scanning after 10 seconds.
        private val SCAN_PERIOD: Long = 5000
        private val PERMISSION_REQUEST_COARSE_LOCATION = 1
    }
}