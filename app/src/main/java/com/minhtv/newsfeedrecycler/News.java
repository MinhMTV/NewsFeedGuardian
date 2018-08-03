package com.minhtv.newsfeedrecycler;

public class News {
    /**
     * Webtitle of News
     */
    private String mtitle;

    /**
     * Section of the News
     */
    private String msection;

    /**
     * time of the News
     */
    private String mauthor;

    /**
     * time of the News
     */
    private String mdate;

    /**
     * WebURL of the News
     */
    private String murl;

    /**
     * Constructs a new EducationNews object.
     *
     * @param title   is news title
     * @param section is the section of the news
     * @param author  is author name
     * @param date    is news date publishing
     * @param url     is news URL
     */

    public News(String title, String section, String author, String date, String url) {
        mtitle = title;
        msection = section;
        mauthor = author;
        mdate = date;
        murl = url;
    }

    /**
     * Returns the news title.
     */
    public String getNewsTitle() {
        return mtitle;
    }

    /**
     * Returns the news category.
     */
    public String getNewsCategory() {
        return msection;
    }

    /**
     * Returns the name of author.
     */
    public String getNewsAuthor() {
        return mauthor;
    }

    /**
     * Returns publishing date.
     */
    public String getNewsDate() {
        return mdate;
    }

    /**
     * Returns the news URL.
     */
    public String getUrl() {
        return murl;
    }
}
