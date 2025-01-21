package com.example.a23210202036;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SliderAdapter sliderAdapter;
    private DistrictAdapter districtAdapter;
    private DistrictDatabase districtDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "Uygulama başlatıldı.");

        // SQLite veritabanı başlatma
        districtDatabase = new DistrictDatabase(this);
        districtDatabase.open();

        // İlçe verilerini ekleyelim (eğer veritabanı boşsa)
        addDistrictsIfNeeded();

        // Slider Ayarları
        ViewPager2 imageSlider = findViewById(R.id.imageSlider);
        sliderAdapter = new SliderAdapter();
        imageSlider.setAdapter(sliderAdapter);

        // Slider görsellerini ekleyin
        loadSliderImages();

        // İlçe Listesi Ayarları
        RecyclerView districtRecyclerView = findViewById(R.id.districtRecyclerView);
        districtRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // OnItemClickListener'ı adapter'a geçiriyoruz
        districtAdapter = new DistrictAdapter(this::onItemClick);

        districtRecyclerView.setAdapter(districtAdapter);

        // SQLite veritabanından ilçeleri al
        loadDistricts();

        // Admin Butonu
        Button goToAdminButton = findViewById(R.id.goToAdminButton);
        goToAdminButton.setOnClickListener(v -> showAdminLoginDialog());

        // "Tüm Turistik Yerleri Göster" butonunun tıklama olayını ekliyoruz
        Button goToTouristSitesButton = findViewById(R.id.goToTouristSitesButton);
        goToTouristSitesButton.setOnClickListener(v -> {
            // IlceActivity'yi başlat
            Intent intent = new Intent(MainActivity.this, IlceActivity.class);
            startActivity(intent);
        });

    }

    private void addDistrictsIfNeeded() {
        // Veritabanında ilçeler varsa, eklemeyi atlayalım
    }

    private void loadSliderImages() {
        // Slider için örnek görseller ekliyoruz
        ArrayList<String> sliderImages = new ArrayList<>();
        sliderImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Uludag.JPG/1200px-Uludag.JPG");
        sliderImages.add("https://www.gotobursa.com.tr/uploads/2021/01/dji_0034_large_1_large.jpg");
        sliderImages.add("https://www.gotobursa.com.tr/uploads/2021/02/dji_0743_large.jpg");

        sliderAdapter.setImages(sliderImages);
    }

    private void loadDistricts() {
        // SQLite veritabanından ilçeleri alıyoruz
        List<DistrictDatabase.District> districtList = districtDatabase.getAllDistricts();

        if (districtList.isEmpty()) {
            Toast.makeText(this, "İlçe verisi bulunamadı!", Toast.LENGTH_SHORT).show();
        } else {
            districtAdapter.setDistricts(districtList); // Veritabanındaki ilçeleri listeye ekliyoruz
        }
    }

    private void onItemClick(String districtName) {
        // Tıklanan ilçenin adını Toast ile göster
        Toast.makeText(this, districtName + " ilçesi tıklandı!", Toast.LENGTH_SHORT).show();

        // İlçenin turistik yerlerine gitmek için IlceActivity'e geçiş yapalım
        Intent intent = new Intent(MainActivity.this, IlceActivity.class);
        intent.putExtra("districtName", districtName);  // İlçe adını IlceActivity'e gönder
        startActivity(intent);
    }

    private void showAdminLoginDialog() {
        // Dialog oluşturma
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_admin_login, null);
        builder.setView(dialogView);

        EditText emailInput = dialogView.findViewById(R.id.emailInput);
        EditText passwordInput = dialogView.findViewById(R.id.passwordInput);
        Button loginButton = dialogView.findViewById(R.id.loginButton);

        AlertDialog dialog = builder.create();

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Sabit kullanıcı bilgileri (örnek)
            String adminEmail = "admin@eadmin.com";
            String adminPassword = "123456";

            if (email.equals(adminEmail) && password.equals(adminPassword)) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();

                // AdminActivity'e geçiş
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Hatalı giriş bilgileri!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Veritabanını kapatalım
        districtDatabase.close();
    }
}
