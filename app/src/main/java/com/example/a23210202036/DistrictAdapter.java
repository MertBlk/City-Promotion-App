package com.example.a23210202036;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder> {

    private List<DistrictDatabase.District> districts = new ArrayList<>(); // Başlangıçta boş bir liste
    private OnItemClickListener onItemClickListener;  // Tıklama olayını dinlemek için bir listener ekliyoruz

    // Verileri adapter'a aktarıyoruz
    public void setDistricts(List<DistrictDatabase.District> districtList) {
        if (districtList == null) return;

        this.districts.clear();  // Önce mevcut verileri temizle
        this.districts.addAll(districtList);  // Yeni verileri ekle
        notifyDataSetChanged();  // RecyclerView'ı güncelle
    }

    // OnItemClickListener arayüzü tanımlıyoruz
    public interface OnItemClickListener {
        void onItemClick(String districtName);
    }

    // Adapter'a listener geçmek için bir constructor ekliyoruz
    public DistrictAdapter(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public DistrictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View'ı inflate et
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.district_item, parent, false);
        return new DistrictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DistrictViewHolder holder, int position) {
        DistrictDatabase.District district = districts.get(position);
        holder.nameTextView.setText(district.getName()); // İlçe adı

        // Eğer nüfus sıfırsa gizleme veya başka bir şey yapabilirsiniz
        if (district.getPopulation() == 0) {
            holder.populationTextView.setVisibility(View.GONE); // Nüfus sıfırsa gösterme
        } else {
            holder.populationTextView.setVisibility(View.VISIBLE); // Nüfus sıfır değilse göster
            holder.populationTextView.setText(String.valueOf(district.getPopulation())); // İlçe nüfusu
        }

        // Tıklama olayını buraya ekliyoruz
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                String districtName = district.getName();  // Tıklanan ilçenin adı
                onItemClickListener.onItemClick(districtName);  // Listener'a ilet
            }
        });
    }

    @Override
    public int getItemCount() {
        return districts.size(); // Liste uzunluğunu döndür
    }

    // ViewHolder sınıfı
    public static class DistrictViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView populationTextView;

        public DistrictViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.districtName);
            populationTextView = itemView.findViewById(R.id.districtPopulation);
        }
    }
}
