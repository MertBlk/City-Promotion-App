package com.example.a23210202036;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DistrictDatabase {
    public void clearAllDistricts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);  // Tabloyu tamamen temizler
        Log.d("DistrictDatabase", "All districts have been cleared.");
    }

    private static final String DB_NAME = "districts_db";
    private static final int DB_VERSION = 2;  // Versiyon numarasını 2 yaptık
    private static final String TABLE_NAME = "districts";
    private SQLiteDatabase database;
    private DistrictHelper dbHelper;

    // District tablosundaki sütunlar
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_POPULATION = "population";  // Nüfus ekleniyor

    // District verilerini temsil eden sınıf
    public static class District {
        private long id;
        private String name;
        private int population;  // Nüfus ekleniyor

        public District(long id, String name, int population) {
            this.id = id;
            this.name = name;
            this.population = population;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getPopulation() {
            return population;
        }
    }

    // Veritabanı yardımcı sınıf
    private static class DistrictHelper extends android.database.sqlite.SQLiteOpenHelper {

        public DistrictHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Districts tablosunu oluşturuyoruz
            String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_POPULATION + " INTEGER);";  // Nüfus ekleniyor
            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Veritabanı versiyonu yükseldiğinde eski tabloyu siliyoruz ve yeni tabloyu oluşturuyoruz
            if (oldVersion < 2) {
                // population sütununu ekliyoruz
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_POPULATION + " INTEGER;");
                Log.d("DatabaseUpgrade", "Population column added successfully.");
            }
        }
    }

    public DistrictDatabase(Context context) {
        dbHelper = new DistrictHelper(context);
    }

    // Veritabanını açıyoruz
    public void open() throws SQLException {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    // Veritabanını kapatıyoruz
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    // İlçenin var olup olmadığını kontrol et (ad ve nüfus kontrolü ekledik)
    public boolean isDistrictExists(String name, int population) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_NAME + " = ?", new String[]{name}, null, null, null);
        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    // Get the count of districts in the database
    public int getDistrictCount() {
        SQLiteDatabase db = this.database;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // District ekleme (Nüfus parametresi eklendi)
    public long addDistrict(String name, int population) {
        if (name == null || name.trim().isEmpty()) {
            Log.d("DistrictDatabase", "District name is invalid.");
            return -1;  // İlçe adı boş olamaz
        }
        if (population <= 0) {
            Log.d("DistrictDatabase", "Invalid population: " + population);
            return -1;  // Nüfus sıfır ya da negatif olamaz
        }

        // Check if the number of districts exceeds 6
        int districtCount = getDistrictCount();
        if (districtCount >= 6) {
            Log.d("DistrictDatabase", "Cannot add more districts. Maximum limit reached.");
            return -1;  // En fazla 6 ilçe eklenebilir
        }

        name = name.trim().toLowerCase();  // Normalize the name to lowercase
        if (isDistrictExists(name, population)) {
            Log.d("DistrictDatabase", "This district already exists: " + name + " with population " + population);
            return -1;  // İlçe zaten var, ekleme yapılmaz
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_POPULATION, population);  // Nüfus bilgisini de ekliyoruz
        return database.insert(TABLE_NAME, null, values);
    }

    // Tüm district'leri al
    public List<District> getAllDistricts() {
        List<District> districtList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") int population = cursor.getInt(cursor.getColumnIndex(COLUMN_POPULATION));  // Nüfus alınıyor
                districtList.add(new District(id, name, population));
            }
            cursor.close();
        }

        return districtList;
    }

    // ID ile district al
    public District getDistrictById(long id) {
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") int population = cursor.getInt(cursor.getColumnIndex(COLUMN_POPULATION));  // Nüfus alınıyor
            cursor.close();
            return new District(id, name, population);
        }
        return null;
    }

    // İlçe adıyla arama yaparak, ilçeleri döndürür
    public List<District> getDistrictsByName(String name) {
        List<District> districtList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_NAME + " LIKE ?", new String[]{"%" + name + "%"}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String districtName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") int population = cursor.getInt(cursor.getColumnIndex(COLUMN_POPULATION));  // Nüfus alınıyor
                districtList.add(new District(id, districtName, population));
            }
            cursor.close();
        }

        return districtList;
    }

    public void logAllDistricts() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") int population = cursor.getInt(cursor.getColumnIndex(COLUMN_POPULATION));
                Log.d("DistrictDatabase", "District: " + name + ", Population: " + population);
            }
            cursor.close();
        }
    }

    public int deleteDistrict(String districtName) {
        return database.delete("districts", "name = ?", new String[]{districtName});
    }
}
