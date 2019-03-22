package org.plc.cetification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Cancion extends Persistable<Cancion> {
    private int id;
    private int position;
    private String name;
    private Album album;
    private static final String insert = "INSERT INTO cancion(id, album, name, number) values(?,?,?,?)";
    private static final String byId = "SELECT * FROM cancion WHERE id=?";
    private static final String byWordInName = "SELECT * FROM cancion WHERE name LIKE ?";
    private static final String byAlbumName = "SELECT c.id as id,c.name as name, c.album as album, c.number as number FROM cancion c JOIN album a ON a.id = c.album WHERE a.name IN ";

    public static Cancion insert(int id, int position, String name, Album album, Connection connection) {
        Cancion cancion = new Cancion();
        cancion.insert(insert, connection,
                new Parameter(Parameter.ParameterType.Int, id),
                new Parameter(Parameter.ParameterType.Int, album.getId()),
                new Parameter(Parameter.ParameterType.String, name),
                new Parameter(Parameter.ParameterType.Int, position)
        );
        cancion.loadById(byId, id, connection);
        return cancion;
    }

    public Cancion() {
        super(Cancion.class);
    }

    @Override
    void load(ResultSet resultSet, Connection connection) throws SQLException {
        id = resultSet.getInt("id");
        name = resultSet.getString("name");
        position = resultSet.getInt("number");
        album = Album.getById(resultSet.getInt("album"), connection);
    }

    public static List<Cancion> getByAlbumns(List<Album> albums, Connection connection) {

        List<Integer> albumIds = albums.stream().map(Album::getId).collect(Collectors.toList());
        String select = "SELECT * FROM cancion WHERE album IN ";
        Cancion cancion = new Cancion();

        List<Cancion> songs = cancion.query(select, connection,
                new Parameter(Parameter.ParameterType.IN, new Values(Values.Type.INT, albumIds)));

        return songs;
    }

    @Override
    public String toString() {
        return String.format(" %3d   %-50s  %-30s %-20s", position, name, album.getName(), album.getAuthor().getName());
    }

    public static List<Cancion> getByName(String wordInName, LikeType likeType, Connection connection) {
        Cancion cancion = new Cancion();
        List<Cancion> cancions = cancion.query(byWordInName, connection,
                new Parameter(Parameter.ParameterType.Like, wordInName, LikeType.Contains));

        return cancions;
    }

    public static List<Cancion> getByAlbumName(Connection connection, String... albumNames) {
        Cancion cancion = new Cancion();
        Parameter parameter = new Parameter(Parameter.ParameterType.IN, new Values(Values.Type.STRING, Arrays.asList(albumNames)));
        List<Cancion> cancions = cancion.query(byAlbumName, connection, parameter);

        return cancions;
    }
}
