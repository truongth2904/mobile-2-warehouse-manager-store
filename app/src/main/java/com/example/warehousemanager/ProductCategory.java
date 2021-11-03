package com.example.warehousemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.warehousemanager.Category.Category;
import com.example.warehousemanager.Category.CategoryAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductCategory extends AppCompatActivity {
    private EditText edtTenLoai;
    private ListView lvDanhSachLoai;
    private Button btnThem,btnXoa,btnSua,btnBack;
    private Context context;
    private ArrayList<Category> categories;
    private CategoryAdapter categoryAdapter;
    ArrayList<String> mKey = new ArrayList<>();
    DatabaseReference DataCategory;
    Category categorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_category);

        setControll();
        setEvent();
    }

    private void setEvent() {
        //
        DataCategory = FirebaseDatabase.getInstance().getReference();
        //
        context = this;
        //
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(context,R.layout.items_product_category,categories);
        lvDanhSachLoai.setAdapter(categoryAdapter);
        //
        DataCategory.child("categorys").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Category category = dataSnapshot.getValue(Category.class);
                categories.add(category);
                categoryAdapter.notifyDataSetChanged();
                mKey.add(dataSnapshot.getKey());
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int index = mKey.indexOf(dataSnapshot.getKey());
                categories.set(index,dataSnapshot.getValue(Category.class));
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                categories.remove(dataSnapshot);
                mKey.remove(dataSnapshot.getKey());
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lvDanhSachLoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected = categories.get(i);
                edtTenLoai.setText(categories.get(i).getName());
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtTenLoai.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Vui Lòng Nhập Dữ Liệu Đầy Đủ", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(checkLoopData(edtTenLoai.getText().toString()))
                    {
                        String idCategory = (categories.size()+1) + "";
                        String name = edtTenLoai.getText().toString();
                        Category category = new Category(idCategory,name);
                        DataCategory.child("categorys").child(idCategory).setValue(category);
                    }else
                    {
                        Toast.makeText(context, "Tên Loại Đồ Uống Đã Tồn Tại", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categorySelected == null)
                {
                    Toast.makeText(context, "Vui Lòng Chọn Đối Tượng Trước Khi Xóa", Toast.LENGTH_SHORT).show();
                }else
                {
                    edtTenLoai.setText("");
                    DataCategory.child("categorys").child(categorySelected.getId()).setValue(null);
                    categories.remove(categorySelected);
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtTenLoai.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Vui Lòng Nhập Dữ Liệu Đầy Đủ", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(checkLoopData(edtTenLoai.getText().toString()))
                    {
                        String name = edtTenLoai.getText().toString();
                        DataCategory.child("categorys").child(categorySelected.getId()).child("name").setValue(name);
                    }else
                    {
                        Toast.makeText(context, "Tên Loại Đồ Uống Đã Tồn Tại", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductCategory.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private Boolean checkLoopData(String data)
    {
        for (Category a:categories
             ) {
            if(a.getName().toString().equals(data))
            {
                return false;
            }
        }
        return true;
    }
    private void  setControll() {
        edtTenLoai = findViewById(R.id.edtTenHangProductCategory);
        lvDanhSachLoai = findViewById(R.id.lvCategoryProduct);
        btnThem = findViewById(R.id.btnThemCategoryProduct);
        btnXoa = findViewById(R.id.btnXoaCategoryProduct);
        btnSua = findViewById(R.id.btnSuaCategoryProduct);
        btnBack = findViewById(R.id.backProductCategory);
    }
}