package com.bionix;

import com.bionix.dao.AuthorDao;
import com.bionix.dao.BookDao;
import com.bionix.dao.BookReviewDao;
import com.bionix.dao.UserDao;
import com.bionix.models.Author;
import com.bionix.models.Book;
import com.bionix.models.BookReview;
import com.bionix.models.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SqlLibraryRepository implements ILibraryRepository {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final UserDao userDao;
    private final BookReviewDao bookReviewDao;

    public SqlLibraryRepository(Connection connection, UserDao userDao, BookReviewDao bookReviewDao) {
        bookDao = new BookDao(connection);
        authorDao = new AuthorDao(connection);
        this.userDao = userDao;
        this.bookReviewDao = bookReviewDao;
    }

    @Override
    public Collection<Book> getAllBooks() {
        return bookDao.getAll();
    }

    @Override
    public Collection<Author> getAllAuthors() {
        return authorDao.getAll();
    }

    @Override
    public Collection<BookReview> getAllReviews() {
        return BookReviewDao.getALL();
    }

    @Override
    public Collection<User> getAllUsers() {
        return UserDao.getALL();
    }

    @Override
    public Optional<Author> getAuthorById(int authorId) {
        return authorDao.getById(authorId);
    }

    @Override
    public Collection<BookAuthor> getAllBooksWithAuthors() {
        Collection<BookAuthor> result = new ArrayList<>();

        Collection<Book> books = bookDao.getAll();
        for (Book book : books) {
            Optional<Author> author = authorDao.getById(book.authorId);
            if (!author.isPresent()) {
                throw new IllegalStateException("Author is missing: " + book.authorId);
            }

            result.add(new BookAuthor(book, author.get()));
        }

        return result;
    }

    @Override
    public Collection<AuthorBooks> getAllAuthorsWithBooks() {
        Collection<AuthorBooks> result = new ArrayList<>();

        Collection<Author> authors = authorDao.getAll();
        for (Author author : authors) {
            Collection<Book> books = bookDao.getAllByAuthorId(author.id);

            result.add(new AuthorBooks(author, books));
        }

        return result;
    }

    @Override
    public Collection<BookBookReview> getAllBookBookReviews() {
        Collection<BookBookReview> result = new ArrayList<>();

        Collection<Book> books = bookDao.getAll();
        for (Book book : books) {
            Optional<Book> bookReview = bookReviewDao.getById(book.id);
            if (!bookReview.isPresent()) {
                throw new IllegalStateException("Author is missing: " + book.authorId);
            }

            result.add(new BookBookReview(book, bookReview.get()));
        }

        return result;
    }

    @Override
    public Collection<BookUsersBookReviews> getAllUsersBookBookReviews() {
        Collection<BookUsersBookReviews> result = new ArrayList<>();

        Collection<User> users = userDao.getAll();
        for (User user : users) {
            Collection<Book> books = userDao.getAllById(user.id);

            result.add(new AuthorBooks(author, books));
        }

        for (Author author : authors) {

        }

        return result;
    }
}
