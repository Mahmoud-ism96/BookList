package com.example.android.booklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.book_t);
        bookTitle.setText(currentBook.getTitle());

        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.book_a);
        bookAuthor.setText(currentBook.getAuthors());

        ImageView bookThumbnail = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
        String image = currentBook.getThumbnail();
        if (image.equals("nopic")) {
            bookThumbnail.setImageResource(R.drawable.nopic);
        } else
            Picasso.get().load(currentBook.getThumbnail()).into(bookThumbnail);


        String url = currentBook.getUrl();

        return listItemView;
    }
}
