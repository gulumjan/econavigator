package com.example.econavigator.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.econavigator.R;
import com.example.econavigator.utils.Constants;

public class MapFragment extends Fragment {

    private Button btnOpenInMaps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        btnOpenInMaps = view.findViewById(R.id.btn_open_in_maps);
        btnOpenInMaps.setOnClickListener(v -> openInExternalMaps());

        return view;
    }


    private void openInExternalMaps() {
        // Создаем Intent для открытия карт
        String uri = String.format("geo:%f,%f?q=%f,%f(%s)",
                Constants.SCHOOL_LATITUDE,
                Constants.SCHOOL_LONGITUDE,
                Constants.SCHOOL_LATITUDE,
                Constants.SCHOOL_LONGITUDE,
                Constants.SCHOOL_NAME);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

        // Проверяем, есть ли приложения для открытия карт
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Если нет - открываем в браузере
            String browserUri = String.format("https://www.google.com/maps/search/?api=1&query=%f,%f",
                    Constants.SCHOOL_LATITUDE,
                    Constants.SCHOOL_LONGITUDE);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(browserUri));
            startActivity(browserIntent);
        }
    }
}