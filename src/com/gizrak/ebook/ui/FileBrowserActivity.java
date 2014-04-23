
package com.gizrak.ebook.ui;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gizrak.ebook.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FileBrowserActivity extends ListActivity {

    private FileListAdapter mAdapter;
    private File mCurrentDir;

    private static final FileFilter mFileFilter = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            // Filters hidden file
            if (name.startsWith(".")) {
                return false;
            }

            // Filters not support files
            if (pathname.isDirectory() ||
                    FileUtils.SUPPORT_FILE_LIST.contains(
                            FileUtils.getFileExtension(pathname.getName()))) {
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listview = getListView();
        mAdapter = new FileListAdapter(this);
        listview.setAdapter(mAdapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Load initial file list
        // TODO Load a saved directory previously
        loadFileList(Environment.getExternalStorageDirectory());
    }

    private void loadFileList(File dir) {
        mCurrentDir = dir;
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(mCurrentDir
                .listFiles(mFileFilter)));
        Collections.sort(files);
        if (mCurrentDir.getParentFile() != null) {
            files.add(0, new File(".."));
        }
        mAdapter.clear();
        mAdapter.addAll(files);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = mAdapter.getItem(position);
        if (file.getName().equals("..")) {
            // load file list of selected file's parent
            loadFileList(mCurrentDir.getParentFile());
        } else if (file.isDirectory()) {
            // load file list of selected file
            loadFileList(file);
        }
    }

    private static class FileListAdapter extends ArrayAdapter<File> {

        private static final int VIEW_TYPE_DIR = 0;
        private static final int VIEW_TYPE_FILE = 1;

        private LayoutInflater mLayoutInflater;

        public FileListAdapter(Context context) {
            super(context, View.NO_ID);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                int resource;
                if (getItemViewType(position) == VIEW_TYPE_DIR) {
                    resource = android.R.layout.simple_list_item_1;
                } else {
                    resource = android.R.layout.simple_list_item_multiple_choice;
                }
                view = mLayoutInflater.inflate(resource, parent, false);
            }

            ((TextView) view).setText(getItem(position).getName());
            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // Has different view type by file type
            return getItem(position).isDirectory() ? VIEW_TYPE_DIR : VIEW_TYPE_FILE;
        }
    }
}
