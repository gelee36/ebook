
package com.gizrak.ebook.ui;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.gizrak.ebook.model.BookItem;
import com.gizrak.ebook.ver2.R;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipFile;

public class BookActivity extends BaseActivity {

    private BookItem mBook;
    private int mCurrentChapter = 0;
    private ZipFile mBookFile;

    private WebView mWebView;

    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mGestureDetectorListener = new GestureDetector.SimpleOnGestureListener() {

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            if (velocityX < 0) {
                mCurrentChapter++;
            } else {
                mCurrentChapter--;
            }

            if (mCurrentChapter < 0) {
                mCurrentChapter = 0;
                return false;
            }

            if (mCurrentChapter >= mBook.getToc().size()) {
                mCurrentChapter = mBook.getToc().size() - 1;
                return false;
            }

            InputStream is;
            try {
                is = mBookFile.getInputStream(mBookFile.getEntry(mBook.getToc()
                        .get(mCurrentChapter)
                        .getContent()));
                mWebView.loadData(CharStreams.toString(new InputStreamReader(is, "utf-8")),
                        "text/html", "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        mBook = getIntent().getParcelableExtra(Constant.EXTRA_BOOK);
        try {
            mBookFile = new ZipFile(mBook.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mGestureDetector = new GestureDetector(this, mGestureDetectorListener);

        mWebView = (WebView) findViewById(R.id.webview);
        InputStream is;
        try {
            is = mBookFile.getInputStream(mBookFile.getEntry(mBook.getToc().get(mCurrentChapter)
                    .getContent()));
            mWebView.loadData(CharStreams.toString(new InputStreamReader(is, "utf-8")),
                    "text/html", "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        if (mBookFile != null) {
            try {
                mBookFile.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
