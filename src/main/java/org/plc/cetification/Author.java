package org.plc.cetification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Author extends Persistable<Author> {
    private int id;
    private String name;
    private List<Album> albums;
    private static final String insert = "INSERT INTO author(id,name) VALUES (?,?);";
    private static final String byId = "SELECT * FROM author WHERE id=?";
    private static final String byName = "SELECT * FROM author WHERE name LIKE ?";
    private static final String authorBySongName = "SELECT DISTINCT a.id AS id, a.name AS name FROM author a " +
            "JOIN album al ON a.id=al.author " +
            "JOIN cancion c ON al.id = c.album " +
            "WHERE c.name = ?";
    private static final String all = "SELECT * FROM author";

    public static Author insert(int id, String name, Connection connection) {
        Author author = new Author();
        author.insert(insert, connection, new Parameter(Parameter.ParameterType.Int, id), new Parameter(Parameter.ParameterType.String, name));
        author.loadById(byId, id, connection);
        return author;
    }

    public Author() {
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
        author.loadById(byId, id, connection);
        return author;
    }

    public static List<Author> getByName(String authorName, Connection connection) {
        return new Author().query(byName, connection, new Parameter(Parameter.ParameterType.String, authorName));
    }

    public static List<Author> getBySongName(String songName, Connection connection) {
        return new Author().query(authorBySongName, connection, new Parameter(Parameter.ParameterType.String, songName));
    }

    @Override
    public String toString() {
        return "Autor: " + name;
    }

    public static List<Author> getAll(Connection connection) {
        return new Author().query(all, connection);
    }
}
