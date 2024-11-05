package com.example.sqllitekotlinexample

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqllitekotlinexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sqlDatabaseHelper: SqlDatabaseHelper
    private var selectedItemForListView: Int? = null // Seçili öğenin ID'si
    private var lastSelectedPosition: Int = -1 // Önceki seçili pozisyon
    private lateinit var userAdapter: UserAdapter // RecyclerView adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sqlDatabaseHelper = SqlDatabaseHelper(this)
        setContentView(binding.root)

        loadUserList() // ListView için kullanıcıları yükle
        setupRecyclerView() // RecyclerView'ı ayarla

        binding.btngoruntule.setOnClickListener {
            loadUserList()
            updateRecyclerView()
        }

        binding.btnekle.setOnClickListener {
            sqlDatabaseHelper.insertUser("uzay", "4267462", R.drawable.profil)
            loadUserList()
            updateRecyclerView()
        }

        binding.btnsil.setOnClickListener {
            if (selectedItemForListView != null) {
                sqlDatabaseHelper.deleteUser(selectedItemForListView!!)
                loadUserList()
                updateRecyclerView()
                selectedItemForListView = null
                lastSelectedPosition = -1 // Seçili pozisyonu sıfırla
            } else {
                Toast.makeText(this, "Silmek için bir kullanıcı seçin.", Toast.LENGTH_SHORT).show()
            }
        }

        // Kullanıcıyı seçmek için listeye tıklanıldığında
        binding.listviewuser.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // Önceki seçili öğenin arka plan rengini sıfırla
            if (lastSelectedPosition != -1) {
                parent.getChildAt(lastSelectedPosition).setBackgroundColor(Color.TRANSPARENT)
            }

            // Seçili öğeyi güncelle
            selectedItemForListView = (parent.getItemAtPosition(position) as User).id
            lastSelectedPosition = position // Yeni seçili pozisyonu güncelle

            // Yeni seçili öğenin arka plan rengini açık yeşil yap
            view.setBackgroundColor(Color.parseColor("#A0DAB9"))
        }

        binding.btnguncelle.setOnClickListener {
            if (selectedItemForListView != null) {
                sqlDatabaseHelper.updateUser(selectedItemForListView!!, "aygun", "4267462", R.drawable.profil)
                loadUserList()
                updateRecyclerView()
                selectedItemForListView = null
                lastSelectedPosition = -1 // Seçili pozisyonu sıfırla
            } else {
                Toast.makeText(this, "Güncellemek için bir kullanıcı seçin.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserList() {
        val list = sqlDatabaseHelper.getAllUser()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binding.listviewuser.adapter = adapter
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        updateRecyclerView() // Başlangıçta verileri yükle
    }

    private fun updateRecyclerView() {
        val userList = sqlDatabaseHelper.getAllUser() // Kullanıcı listesini al
        userAdapter = UserAdapter(userList) // UserAdapter ile listeyi bağla
        binding.recyclerView.adapter = userAdapter

        // Tıklama olayını ayarla
        userAdapter.setOnItemClickListener { user ->
            // Burada tıklanan kullanıcının ID'sini al
            selectedItemForListView = user.id
            Toast.makeText(this, "Seçilen kullanıcı ID: $selectedItemForListView", Toast.LENGTH_SHORT).show()
        }
    }
}
