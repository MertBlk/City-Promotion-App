package com.example.a23210202036;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class IlceActivity extends AppCompatActivity {

    private ListView touristSitesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilce);

        touristSitesList = findViewById(R.id.touristSitesList);

        // İlçeler ve turistik yerler listesi
        ArrayList<String> touristSites = new ArrayList<>();

        // Osmangazi ilçesi
        touristSites.add("Osmangazi İlçesi: Ulu Camii, Bursa Kalesi, Tophane, Koza Han");

        // Nilüfer ilçesi
        touristSites.add("Nilüfer İlçesi: Ataevler Parkı, Fethiye Camii, Nilüfer Organize Sanayi Bölgesi");

        // Yıldırım ilçesi
        touristSites.add("Yıldırım İlçesi: Yıldırım Camii, Muradiye Külliyesi");

        // Mudanya ilçesi
        touristSites.add("Mudanya İlçesi: Mudanya Sahili, Giritli Evleri, Mudanya Mütarekesi Müzesi");

        // İnegöl ilçesi
        touristSites.add("İnegöl İlçesi: İnegöl Müzesi, Oylat Kaplıcaları, İnegöl Kent Ormanı");

        // Gemlik ilçesi
        touristSites.add("Gemlik İlçesi: Gemlik Sahili, Eski Hamam, Gemlik Belediyesi Parkı");

        // ArrayAdapter ile listeyi göster
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, touristSites);
        touristSitesList.setAdapter(adapter);
    }
}
