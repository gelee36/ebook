
package com.gizrak.ebook.controller;

import android.provider.BaseColumns;

public class BookConstract {
    private BookConstract() {
    }

    public static class BookColumns implements BaseColumns {
        private BookColumns() {
        }

        public static final String TYPE = "book_type";
        public static final String TITLE = "book_title";
        public static final String AUTHOR = "book_author";
        public static final String PUBLISHER = "book_publisher";
        public static final String ISBN = "book_isbn";
        public static final String LANGUAGE = "book_language";
        public static final String COVER = "book_image";
        public static final String PATH = "book_path";
        public static final String CUR_CHAP = "book_curChapter";
        public static final String CUR_PAGE = "book_curPage";
        public static final String CUR_PCNT = "book_curPercentage";
    }

    public static class TocColumns implements BaseColumns {
        private TocColumns() {
        }

        public static final String BID = "toc_book_id";
        public static final String SEQ = "toc_seq";
        public static final String TITLE = "toc_title";
        public static final String URL = "toc_url";
        public static final String ANCHOR = "toc_anchor";
    }

    public static class BookmarkColumns implements BaseColumns {
        private BookmarkColumns() {
        }

        public static final String BID = "bm_book_id";
        public static final String CHAPTER = "bm_chapter";
        public static final String PERCENTAGE = "bm_percentage";
    }
}
