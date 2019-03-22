package org.plc.cetification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Author extends Persistable<Author> {
    private int id;
    private String name;
    private List<Album> albums;
    private static final String insert = "INSERT INTO author(id,name) VALUES (?,?);";
    private static final String byId = "SELECT * FROM author WHERE id=?";

    public static Author insert(int id, String name, Connection connection) {
        Author author = new Author();
        author.insert(insert, connection, new Parameter(Parameter.ParameterType.Int, id), new Parameter(Parameter.ParameterType.String, name));
        author.getById(byId, id, connection);
        return author;
    }

    private Author() {
        super(Author.class);
    }

    @Override
    void load(ResultSet resultSet, Connection connection) throws SQLException {
        id = resultSet.getInt("id");
        name = resultSet.getString("name");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Author getById(int id, Connection connection) {
        Author author = new Author();
        author.getById(byId, id, connection);
        return author;
    }

    public static List<Author> getByName(String authorName, Connection connection) {
        String select = "SELECT * FROM author WHERE name LIKE ?";
        List<Author> authors = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(select)) {
            int pos = 1;
            statement.setString(pos, "%" + authorName + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Author a = new Author();
                a.load(rs, connection);
                authors.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public static List<Author> getBySongName(String songName, Connection connection) {
        String select = "SELECT DISTINCT a.id AS id, a.name AS name FROM author a " +
                "JOIN album al ON a.id=al.author " +
                "JOIN cancion c ON al.id = c.album " +
                "WHERE c.name = ?";
        List<Author> authors = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(select)) {
            int pos = 1;
            statement.setString(pos, songName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Author a = new Author();
                a.load(rs, connection);
                authors.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public String toString() {
        return "Autor: " + name;
    }

    public static List<Author> getAll(Connection connection) {
        String select = "SELECT * FROM author";
        List<Author> authors = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(select)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Author a = new Author();
                a.load(rs, connection);
                authors.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }
}
