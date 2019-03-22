package org.plc.cetification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Album extends Persistable<Album> {
    private int id;
    private String name;
    private int year;
    private Author author;
    private static final String insert = "INSERT INTO album(id, author, name, year) values(?,?,?,?)";
    private static final String byId = "SELECT * FROM album WHERE id=?";
    private static final String byYear = "SELECT * FROM album WHERE year=?";


    public Album() {
        super(Album.class);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Author getAuthor() {
        return author;
    }

    public static Album getNewAlbum(int id, String name, int year, Author author, Connection connection) {
        Album album = new Album();
        album.insert(
                insert, connection,
                new Parameter(Parameter.ParameterType.Int, id),
                new Parameter(Parameter.ParameterType.Int, author.getId()),
                new Parameter(Parameter.ParameterType.String, name),
                new Parameter(Parameter.ParameterType.Int, year)
        );
        album.getById(byId, id, connection);
        return album;
    }

    static Album getById(int id, Connection connection) {
        Album album = new Album();
        album.getById(byId, id, connection);
        return album;
    }

    @Override
    void load(ResultSet resultSet, Connection connection) throws SQLException {
        id = resultSet.getInt("id");
        name = resultSet.getString("name");
        year = resultSet.getInt("year");
        author = Author.getById(resultSet.getInt("author"), connection);
    }

    public static List<Album> getByAuthors(List<Author> authors, Connection connection) {
        List<Integer> authorIds = authors.stream().map(Author::getId).collect(Collectors.toList());
        String select = "SELECT * FROM album WHERE author IN ";
        Album album = new Album();
        Parameter parameter = new Parameter(Parameter.ParameterType.IN, new Values(Values.Type.INT, authorIds));
        return album.query(select, connection, parameter);

    }

    @Override
    public String toString() {
        return String.format("%5d %-30s %-20s ",
                year, name, author.getName()

        );
    }

    public static List<Album> getByYear(int year, Connection connection) {
        return new Album().query(byYear, connection, new Parameter(Parameter.ParameterType.Int, year));
    }
}
