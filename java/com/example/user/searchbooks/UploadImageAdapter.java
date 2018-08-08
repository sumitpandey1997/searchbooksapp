package com.example.user.searchbooks;

/**
 * Created by user on 12-07-2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {
    private AdapterView.OnItemClickListener mItemClickListener;
    public ArrayList<String> imageDoneList;
    public ArrayList<String> fileNameList;
    Context context;

    public UploadImageAdapter(Context context, ArrayList<String> imageDoneList, ArrayList<String> fileNameList) {
        this.imageDoneList = imageDoneList;
        this.fileNameList = fileNameList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_images,parent,false);

        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String filename=fileNameList.get(position);
        holder.imageName.setText(filename);

        String fileDone = imageDoneList.get(position);

        if(fileDone.equals("uploading")){

            holder.imageCheck.setImageResource(R.drawable.pending);

        } else {

            holder.imageCheck.setImageResource(R.drawable.done);

        }
        holder.cutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileNameList.size()>1) {
                    fileNameList.remove(position);
                    imageDoneList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, fileNameList.size());
                    notifyItemRangeChanged(position, imageDoneList.size());
                    view.bringToFront();
                }

                else
                {
                    Toast.makeText(context, "last image remained keep atleast one image", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        View mView;
        TextView imageName;
        ImageView imageCheck;
        ImageView cutIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageName = mView.findViewById(R.id.imageName);
            imageCheck = mView.findViewById(R.id.checkImage);
            cutIcon = mView.findViewById(R.id.cut);

            //cutIcon.setOnClickListener(this);

        }


        private void removeAt(int position) {
            imageDoneList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, imageDoneList.size());
        }

    }


}
