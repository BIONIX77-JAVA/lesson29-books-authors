package com.bionix.dao;

import com.bionix.models.Author;
import com.bionix.models.Book;
import com.bionix.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Реализация DAO для модели {@link Author}.
 */
public class UserDao {
    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public static Collection<User> getALL() {
    }

    /**
     * Creates table for authors unless if it does not exist.
     */
    public void createTableIfAbsent() {
        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `authors`(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name VARCHAR(100)" +
                    ")");

        } catch (SQLException e) {
            throw new AuthorDaoException(e);
        }
    }

    /**
     * Loads all authors from the database.
     *
     * @return all found authors.
     * @throws AuthorDaoException If any SQL-related failure happens here.
     */
    public Collection<Author> getAll() {
        try (Statement statement = connection.createStatement()) {

            Collection<Author> result = new ArrayList<>();

            try (ResultSet cursor = statement.executeQuery(
                    "SELECT * FROM `authors`")) {
                while (cursor.next()) {
                    result.add(createAuthorFromCursor(cursor));
                }
            }

            return result;
        } catch (SQLException e) {
            throw new AuthorDaoException(e);
        }
    }

    public Optional<Author> getById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `authors` WHERE id=?")) {
            statement.setInt(1, id);

            Author result;

            try (ResultSet cursor = statement.executeQuery()) {
                if (cursor.next()) {
                    // есть одна запись в выборке
                    result = createAuthorFromCursor(cursor);
                } else {
                    // нет записей в выборке
                    result = null;
                }
            }

            return Optional.ofNullable(result);
        } catch (SQLException e) {
            throw new AuthorDaoException(e);
        }
    }

    private Author createAuthorFromCursor(ResultSet cursor) throws SQLException {
        int id = cursor.getInt("id");
        String name = cursor.getString("name");
        return new Author(id, name);
    }

    public void insert(Author author) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `authors`(`name`) VALUES(?)")) {
            statement.setString(1, author.name);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.id = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new AuthorDaoException(e);
        }
    }

    public void update(Author author) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE `authors` SET name=? WHERE id=?")) {
            statement.setString(1, author.name);
            statement.setInt(2, author.id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new AuthorDaoException(e);
        }
    }

    public void delete(Author author) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM `authors` WHERE id=?")) {
            statement.setInt(1, author.id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new AuthorDaoException(e);
        }
    }

    public Collection<Author> findByPartialName(String text) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `authors` WHERE `name` LIKE ?")) {
            statement.setString(1, "%" + text + "%");

            Collection<Author> result = new ArrayList<>();

            try (ResultSet cursor = statement.executeQuery()) {
                while (cursor.next()) {
                    result.add(createAuthorFromCursor(cursor));
                }
            }

            return result;
        } catch (SQLException e) {
            throw new AuthorDaoException(e);
        }
    }
}

    /**
     * исключение, в которое оборачиваются другие
     * исключения из нижележащих слоев логики.
     */
    public static class AuthorDaoException extends RuntimeException {
        public AuthorDaoException(Throwable cause) {
            super(cause);
        }
    }
}
