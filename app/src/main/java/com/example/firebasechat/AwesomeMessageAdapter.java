package com.example.firebasechat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class AwesomeMessageAdapter extends ArrayAdapter<AwesomeMessage> {

    private List<AwesomeMessage> messages;
    private Activity activity;

    public AwesomeMessageAdapter(@NonNull Activity activity, int resource, List<AwesomeMessage> messages) {
        super(activity, resource,messages);
        this.messages = messages;
        this.activity = activity;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        AwesomeMessage message = getItem(position);
        int layoutRecourse = 0;
        int viewType = getItemViewType(position);

        if(viewType == 0){
            layoutRecourse = R.layout.my_message_item;
        }else{
            layoutRecourse = R.layout.your_message_item;
        }

        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else{
            convertView = layoutInflater.inflate(layoutRecourse,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        boolean isText = message.getImageUrl() == null;

        if(isText){
            viewHolder.textViewBubble.setVisibility(View.VISIBLE);
            viewHolder.imageViewPhoto.setVisibility(View.GONE);
            viewHolder.textViewBubble.setText(message.getText());
        }else{
            viewHolder.textViewBubble.setVisibility(View.GONE);
            viewHolder.imageViewPhoto.setVisibility(View.VISIBLE);
            viewHolder.textViewBubble.setText(message.getText());
            Glide.with(viewHolder.imageViewPhoto.getContext()).load(message.getImageUrl()).into(viewHolder.imageViewPhoto);

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        int flag;
        AwesomeMessage awesomeMessage = messages.get(position);
        if(awesomeMessage.isMine()){
            flag = 0;
        }else{
            flag = 1;
        }
        return flag;
    }

    @Override
    public int getViewTypeCount() {
         return 2;
    }

    private class ViewHolder{
        private TextView textViewBubble;
        private ImageView imageViewPhoto;

        public ViewHolder(View view){
            imageViewPhoto = view.findViewById(R.id.imageViewMessagePhoto);
            textViewBubble = view.findViewById(R.id.textViewBubble);
        }


    }


}
