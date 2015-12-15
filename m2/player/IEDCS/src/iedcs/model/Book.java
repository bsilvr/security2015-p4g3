package iedcs.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Book.
 */
public class Book {

    private final StringProperty author;
    private final StringProperty title;
    private final StringProperty language;
    private final StringProperty image;
    private final StringProperty id;

    /**
     * Default constructor.
     */
    public Book() {
        this(null, null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param firstName
     * @param lastName
     */
    public Book(String author, String title, String language, String image, String id) {
        this.author = new SimpleStringProperty(author);
        this.title = new SimpleStringProperty(title);
        this.language = new SimpleStringProperty(language);
        this.image = new SimpleStringProperty(image);
        this.id = new SimpleStringProperty(id);
    }

    public String getBookAuthor() {
        return author.get();
    }

    public void setBookAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty bookAuthorProperty() {
        return author;
    }

    public String getBookTitle() {
        return title.get();
    }

    public void setBookTitle(String title) {
        this.title.set(title);
    }

    public StringProperty bookTitleProperty() {
        return title;
    }

    public String getBookLanguage() {
        return language.get();
    }

    public void setBookLanguage(String language) {
        this.language.set(language);
    }

    public StringProperty bookLanguageProperty() {
        return language;
    }

    public String getBookImage() {
        return image.get();
    }

    public void setBookImage(String image) {
        this.image.set(image);
    }

    public StringProperty bookImageProperty() {
        return image;
    }

    public String getBookId() {
        return id.get();
    }

    public void setBookId(String id) {
        this.id.set(id);
    }

    public StringProperty bookIdProperty() {
        return id;
    }

}