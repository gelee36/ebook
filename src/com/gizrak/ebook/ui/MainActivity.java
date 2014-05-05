
package com.gizrak.ebook.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gizrak.ebook.controller.BookDbHelper;
import com.gizrak.ebook.model.BookItem;
import com.gizrak.ebook.utils.ImageUtils;
import com.gizrak.ebook.ver2.R;

public class MainActivity extends ListActivity {

    private static final int ACTIVITY_REQUEST_ADD_BOOK = 100;

    private ListView mListView;
    private BookListAdapter mAdapter;

    private BookDbHelper mBookDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = getListView();

        mBookDbHelper = new BookDbHelper(this);
        mAdapter = new BookListAdapter(this);
        // Load books loaded previously
        mAdapter.addAll(mBookDbHelper.getBookList());
        mListView.setAdapter(mAdapter);
    }

    private void refreshBookList() {
        mAdapter.clear();
        mAdapter.addAll(mBookDbHelper.getBookList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_book:
                Intent intent = new Intent(this, FileBrowserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, ACTIVITY_REQUEST_ADD_BOOK);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_REQUEST_ADD_BOOK:
                if (resultCode == RESULT_OK) {
                    refreshBookList();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra(Constant.EXTRA_BOOK, mAdapter.getItem(position));
        startActivity(intent);
    }

    private static class BookListAdapter extends ArrayAdapter<BookItem> {

        private LayoutInflater mLayoutInflater;

        public BookListAdapter(Context context) {
            super(context, View.NO_ID);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;
            if (view == null) {
                view = mLayoutInflater.inflate(R.layout.bookshelf_item, parent, false);
                holder = new ViewHolder();
                holder.cover = (ImageView) view.findViewById(R.id.cover);
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.author = (TextView) view.findViewById(R.id.author);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            BookItem item = getItem(position);
            holder.cover.setImageBitmap(ImageUtils.toBitmap(item.getCover()));
            holder.title.setText(item.getTitle());
            holder.author.setText(item.getAuthor());
            return view;
        }

        private static class ViewHolder {
            ImageView cover;
            TextView title;
            TextView author;
        }
    }
}
