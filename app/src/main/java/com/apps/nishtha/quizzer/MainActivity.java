package com.apps.nishtha.quizzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Category> categoryArrayList=new ArrayList<>();
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recViewCategory=findViewById(R.id.recViewCategory);
        categoryAdapter = new CategoryAdapter(this, categoryArrayList);
        recViewCategory.setLayoutManager(new GridLayoutManager(this,2));
        recViewCategory.setAdapter(categoryAdapter);

        addCategories();
    }

    private void addCategories() {
        categoryArrayList.add(new Category(getString(R.string.category_mathematics)));
        categoryArrayList.add(new Category(getString(R.string.category_science)));
        categoryArrayList.add(new Category(getString(R.string.category_history)));
        categoryArrayList.add(new Category(getString(R.string.category_geography)));
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //avoids transition from mainActivity to SignInActivity
    }
}