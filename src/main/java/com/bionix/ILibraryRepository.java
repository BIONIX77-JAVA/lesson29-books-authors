package com.bionix;

import com.bionix.models.Author;
import com.bionix.models.Book;
import com.bionix.models.BookReview;
import com.bionix.models.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Abstract interface for repository of books and authors.
 */
public interface ILibraryRepository {
    Collection<Book> getAllBooks();
    Collection<Author> getAllAuthors();
    Collection<BookReview> getAllReviews();
    Collection<User> getAllUsers();

    Collection<BookAuthor> getAllBooksWithAuthors();
    Collection<AuthorBooks> getAllAuthorsWithBooks();
    Collection<BookBookReview> getAllBookBookReviews();
    Collection<BookUsersBookReviews>getAllUsersBookBookReviews();

    Optional<Author> getAuthorById(int authorId);

    class BookAuthor {
        public final Book book;
        public final Author author;

        public BookAuthor(Book book, Author author) {
            this.book = book;
            this.author = author;
        }
    }
    class AuthorBooks {
        public final Author author;
        public final Collection<Book> books;

        public AuthorBooks(Author author, Collection<Book> books) {
            this.author = author;
            this.books = books;
        }
    }
    class BookBookReview{
        public final Book book;
        public final Collection<BookReview> bookReviews;

        public BookBookReview(Book book, Book bookReviews) {
            this.book = book;
            this.bookReviews = bookReviews;
        }
    }
    class BookUsersBookReviews{
        public final Book book;
        public final Collection<User>users;
        public final Collection<BookReview> bookReview;

        public BookUsersBookReviews(Book book, Collection<User> users, Collection<BookReview> bookReview) {
            this.book = book;
            this.users = users;
            this.bookReview = bookReview;
        }
    }
}
