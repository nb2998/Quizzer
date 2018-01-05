package com.apps.nishtha.quizzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nishtha on 23/12/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private static final String TAG = "TAG";
    Context context;
    ArrayList<Category> categoryArrayList;
    ProgressDialog progressDialog;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(context).inflate(R.layout.single_category,parent,false));
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, final int position) {
        if(categoryArrayList!=null){
            holder.tvCategoryName.setText(categoryArrayList.get(position).getName());
            holder.cardCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    getQuestions(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        ImageView ivCategory;
        TextView tvCategoryName;
        CardView cardCategory;
        public CategoryHolder(View itemView) {
            super(itemView);
            ivCategory=itemView.findViewById(R.id.ivCategory);
            tvCategoryName=itemView.findViewById(R.id.tvCategoryName);
            cardCategory=itemView.findViewById(R.id.cardCategory);
        }
    }

    private void getQuestions(Integer category) {
        final ArrayList<QnA> qnAArrayList=new ArrayList<>();
        String baseUrl="https://cocktail-trivia-api.herokuapp.com/api/category/";
        StringBuilder url=new StringBuilder(baseUrl);
        switch(category){
            case 0: //Mathematics
                url.append("science-mathematics");
                break;

            case 1: //Science and nature
                url.append("science-nature");
                break;

            case 2:  //History
                url.append("history");
                break;

            case 3:  //Geography
                url.append("geography");
                break;

            default:
                Log.d(TAG, "getQuestions: no category");
        }
        Log.d(TAG, "getQuestions: url : "+url);

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url.toString())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: failed "+e.getLocalizedMessage());
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Gson gson=new Gson();
                QnA[] qnas=gson.fromJson(response.body().string(),QnA[].class);
                qnAArrayList.addAll(Arrays.asList(qnas));
                Log.d(TAG, "onResponse: item added, size now "+qnAArrayList.size());
//                generateRandomQuestion(qnAArrayList);
                progressDialog.dismiss();
                Intent intent=new Intent(context, QuestionActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("question",qnAArrayList);
//                intent.putExtras(bundle);
                intent.putExtra("qna", qnAArrayList);
                context.startActivity(intent);
            }
        });
    }
}
