package com.minhtv.newsfeedrecycler;

public class News {
    /**
     * Webtitle of News
     */
    private String title;

    /**
     * Section of the News
     */
    private String section;


    /**
     * time of the News
     */
    private String author;

    /**
     * time of the News
     */
    private String date;

    /**
     * WebURL of the News
     */
    private String url;

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
        this.title = title;
        this.section = section;
        this.author = author;
        this.date = date;
        this.url = url;
    }
    /**
     * Returns the news title.
     */
    public String getNewsTitle() {
        return title;
    }

    /**
     * Returns the news category.
     */
    public String getNewsCategory() {
        return section;
    }

    /**
     * Returns the name of author.
     */
    public String getNewsAuthor() {
        return author;
    }

    /**
     * Returns publishing date.
     */
    public String getNewsDate() {
        return date;
    }

    /**
     * Returns the news URL.
     */
    public String getUrl() {
        return url;
    }
}
