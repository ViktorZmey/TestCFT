package com.example.testcft

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcft.adapter.UserDetailAdapter
import com.example.testcft.adapter.UserDetailsItem
import com.example.testcft.databinding.ActivityUserDetailsBinding


class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserDetailsBinding
    private lateinit var adapter: UserDetailAdapter
    private val model: UserDetailsModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindViewModel()
        adapter = UserDetailAdapter(this) { item, position ->
            selectProcess(item.type)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        val id: Long = intent.getLongExtra("userId", -1)
        model.setUserId(id)
    }

    private fun bindViewModel() {
        model.userDetailItemsLiveData.observe(this){
            it.onSuccess { items ->
                adapter.submitList(items)
            }.onFailure { exception ->
                showAlert(exception.localizedMessage)
            }
        }
    }

    private fun selectProcess(type: UserDetailsItem.ContentType) {
        when(type) {
            is UserDetailsItem.ContentType.Email -> { goToSendEmail(type.email) }
            is UserDetailsItem.ContentType.Cell -> { goToCallPhone(type.cell) }
            is UserDetailsItem.ContentType.Phone -> { goToCallPhone(type.phone) }
            is UserDetailsItem.ContentType.Location -> { model.getLocation()?.run { goToMapByLocation(first, second) } }
            is UserDetailsItem.ContentType.Address -> { goToMapByAddress(type.address) }
            else -> {}
        }
    }

    private fun goToSendEmail(email: String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        startActivity(Intent.createChooser(intent, ""))
    }

    private fun goToCallPhone(phone: String){
        val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone}"));
        startActivity(phoneIntent)
    }

    private fun goToMapByLocation(lat: Double, lon: Double){
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${lat},${lon}"))
        startActivity(mapIntent)
    }

    private fun goToMapByAddress(address: String) {
        val uriStr = "http://maps.google.co.in/maps?q=${address}"
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uriStr))
        startActivity(mapIntent)
    }

    private fun showAlert(message : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Title")
        builder.setMessage(R.string.noSuchUser)
        builder.setNeutralButton(R.string.ok) { _, _ ->
            finish()
        }
        builder.show()
    }
}



