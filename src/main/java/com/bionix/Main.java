package com.bionix;

//На основе системы книг и авторов:
//
//        Добавить классы пользователей и отзывов о книгах.
//                     Определить связи между таблицами (один-к-одному или один-ко-многим)
//        Выделить DAO для каждой таблицы (книги, авторы, пользователи, отзывы) с реализацией CRUD операций.
//        Определить и реализовать ILibraryRepository как абстракцию, связывающую DAO книг, авторов, пользователей и отзывов.


import com.bionix.dao.AuthorDao;
import com.bionix.dao.BookDao;
import com.bionix.models.Author;
import com.bionix.models.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:sqlite:library.db")) {
            AuthorDao authorDao = new AuthorDao(connection);
            BookDao bookDao = new BookDao(connection);
            authorDao.createTableIfAbsent();
            bookDao.createTableIfAbsent();

            if (authorDao.findByPartialName("Stroustrup").isEmpty()) {
                Author author = new Author(0, "Stroustrup");
                authorDao.insert(author);
            }
            if (authorDao.findByPartialName("Deitel").isEmpty()) {
                Author author = new Author(0, "Deitel");
                authorDao.insert(author);

                Book book = new Book(0,
                        "Programming with Java",
                        2006, author.id);
                bookDao.insert(book);
            }
            if (authorDao.findByPartialName("Horstmann").isEmpty()) {
                Author author = new Author(0, "Horstmann");
                authorDao.insert(author);

                Book book = new Book(0,
                        "Core Java. Volume 1. Fundamentals",
                        2018, author.id);
                bookDao.insert(book);

                Book book2 = new Book(0,
                        "Core Java. Volume 2. Advanced Features",
                        2018, author.id);
                bookDao.insert(book2);
            }

            Collection<Author> authors = authorDao.getAll();
            System.out.println("Authors count: " + authors.size());
            Collection<Book> books = bookDao.getAll();
            System.out.println("Books count: " + books.size());

            ILibraryRepository repository = null; // TODO initialize
//            List<Book> books = new LinkedList<>(repository.getAllBooks());

            Collection<ILibraryRepository.BookAuthor> booksWithAuthors =
                    repository.getAllBooksWithAuthors();
            for (ILibraryRepository.BookAuthor bookAuthor : booksWithAuthors) {
                System.out.println("Book: " + bookAuthor.book.title);
                System.out.println("Author: " + bookAuthor.author.name);
            }

//            Book book = books.get(0);
//            Author authorOfBook = maybeAuthor.get();
        }
    }
}
