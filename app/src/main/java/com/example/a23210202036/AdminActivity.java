package com.example.a23210202036;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private EditText districtNameInput;
    private EditText districtPopulationInput;  // Nüfus alanı için EditText
    private Button addDistrictButton;
    private Button deleteDistrictButton;       // İlçe silme butonu
    private DistrictDatabase districtDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Görünüm öğelerini başlatıyoruz
        districtNameInput = findViewById(R.id.districtNameInput);
        districtPopulationInput = findViewById(R.id.districtPopulationInput);
        addDistrictButton = findViewById(R.id.addDistrictButton);
        deleteDistrictButton = findViewById(R.id.deleteDistrictButton); // İlçe silme butonu

        // Veritabanı bağlantısını başlatıyoruz
        districtDatabase = new DistrictDatabase(this);
        districtDatabase.open();

        // İlçe ekleme butonuna tıklama olayı
        addDistrictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String districtName = districtNameInput.getText().toString().trim();
                String populationString = districtPopulationInput.getText().toString().trim();

                if (!districtName.isEmpty() && !populationString.isEmpty()) {
                    try {
                        int population = Integer.parseInt(populationString);  // Nüfus bilgisini integer'a çeviriyoruz
                        long id = districtDatabase.addDistrict(districtName, population);  // İlçe ekleme işlemi

                        if (id > 0) {
                            Toast.makeText(AdminActivity.this, "İlçe eklendi: " + districtName, Toast.LENGTH_SHORT).show();
                            districtNameInput.setText("");  // İlçe adı alanını temizliyoruz
                            districtPopulationInput.setText("");  // Nüfus alanını temizliyoruz
                        } else {
                            Toast.makeText(AdminActivity.this, "İlçe eklenemedi!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(AdminActivity.this, "Geçerli bir nüfus girin!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Lütfen bir ilçe adı ve nüfus girin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // İlçe silme butonuna tıklama olayı
        deleteDistrictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String districtName = districtNameInput.getText().toString().trim();

                if (!districtName.isEmpty()) {
                    int rowsDeleted = districtDatabase.deleteDistrict(districtName); // İlçe silme işlemi

                    if (rowsDeleted > 0) {
                        Toast.makeText(AdminActivity.this, "İlçe silindi: " + districtName, Toast.LENGTH_SHORT).show();
                        districtNameInput.setText(""); // İlçe adı alanını temizliyoruz
                    } else {
                        Toast.makeText(AdminActivity.this, "İlçe bulunamadı!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Silmek için bir ilçe adı girin!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Veritabanını kapatıyoruz
        districtDatabase.close();
    }
}
