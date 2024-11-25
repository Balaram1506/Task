package com.example.task_1;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<User> userList;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Set name
        holder.textViewName.setText(user.getName());

        // Set age and premium status
        String agePremiumText = "Age: " + user.getAge() + " | Premium: " + (user.isPremium() ? "Yes" : "No");
        holder.textViewAgePremium.setText(agePremiumText);

        // The premium icon is always shown, no conditional logic needed
        holder.imagePremium.setImageResource(R.drawable.baseline_perm_identity_24); // Example: ic_premium
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewAgePremium;
        ImageView imagePremium;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAgePremium = itemView.findViewById(R.id.textViewAgePremium);
            imagePremium = itemView.findViewById(R.id.imagePremium);
        }
    }
}


