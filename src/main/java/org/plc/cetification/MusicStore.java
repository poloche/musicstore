package org.plc.cetification;

import org.sqlite.SQLiteConfig;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import java.sql.SQLException;

public class MusicStore {
    public static void main(String arg[]) {
        try {
            SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource();
            ds.setUrl("jdbc:sqlite:./musicstore.db");

            SQLiteConfig config = new SQLiteConfig();
            ds.setConfig(config);

            IMusicopedia gs = MusicopediaFactory.getInstance(ds);
            populate(gs);

            System.out.println("*****************************");
            System.out.println("Listing Albums By Author Name");
            gs.getAlbumsByAuthor("Pet Shop Boys").forEach(System.out::println);
            gs.getSongsByAutor("Pet Shop Boys").forEach(System.out::println);
            gs.getAlbumsByYear(1990).forEach(System.out::println);
            gs.getAllSongsThatContain("you").forEach(System.out::println);
            gs.getSongsByAlbum("Depeche Mode", "Violator").forEach(System.out::println);
            gs.getAutorsBySongName("Enjoy the silence").forEach(System.out::println);
            gs.getSongsThatStartWith("N").forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void populate(IMusicopedia gs) throws Exception {
        if (gs.isEmptyDB()) {
            Author psb = gs.agregarAutor("Pet Shop Boys");
            Album psbac = gs.agregarAlbum(psb, "Actually", 1987);
            Album psbbe = gs.agregarAlbum(psb, "Behaviour", 1990);

            gs.agregarCancion(psbac, 1, "One more chance");
            gs.agregarCancion(psbac, 2, "What have I done to deserve this?");
            gs.agregarCancion(psbac, 3, "Shopping");
            gs.agregarCancion(psbac, 4, "Rent");
            gs.agregarCancion(psbac, 5, "Hit music");
            gs.agregarCancion(psbac, 6, "It couldn't happen here");
            gs.agregarCancion(psbac, 7, "It's a sin");
            gs.agregarCancion(psbac, 8, "Wake up");
            gs.agregarCancion(psbac, 9, "Heart");
            gs.agregarCancion(psbac, 10, "King's cross");

            gs.agregarCancion(psbbe, 1, "Being boring");
            gs.agregarCancion(psbbe, 2, "This must be the place I waited years to leave");
            gs.agregarCancion(psbbe, 3, "To face the truth");
            gs.agregarCancion(psbbe, 4, "How can you expect to be taken seriously?");
            gs.agregarCancion(psbbe, 5, "Only the wind");
            gs.agregarCancion(psbbe, 6, "October symphony");
            gs.agregarCancion(psbbe, 7, "So hard");
            gs.agregarCancion(psbbe, 8, "Nervously");
            gs.agregarCancion(psbbe, 9, "The end of the world");
            gs.agregarCancion(psbbe, 10, "Jealousy");

            Author dm = gs.agregarAutor("Depeche Mode");
            Album dmmm = gs.agregarAlbum(dm, "Music for the masses", 1987);
            Album dmvi = gs.agregarAlbum(dm, "Violator", 1990);
            gs.agregarCancion(dmmm, 1, "Never let me down");
            gs.agregarCancion(dmmm, 2, "The things you said");
            gs.agregarCancion(dmmm, 3, "Strangelove");
            gs.agregarCancion(dmmm, 4, "Sacred");
            gs.agregarCancion(dmmm, 5, "Little 15");
            gs.agregarCancion(dmmm, 6, "Behind the wheel");
            gs.agregarCancion(dmmm, 7, "I want you now");
            gs.agregarCancion(dmmm, 8, "To have and to hold");
            gs.agregarCancion(dmmm, 9, "Nothing");
            gs.agregarCancion(dmmm, 10, "Pimpf");

            gs.agregarCancion(dmvi, 1, "World in my eyes");
            gs.agregarCancion(dmvi, 2, "Sweetest perfection");
            gs.agregarCancion(dmvi, 3, "Personal Jesus");
            gs.agregarCancion(dmvi, 4, "Halo");
            gs.agregarCancion(dmvi, 5, "Waiting for the night");
            gs.agregarCancion(dmvi, 6, "Enjoy the silence");
            gs.agregarCancion(dmvi, 7, "Policy of truth");
            gs.agregarCancion(dmvi, 8, "Blue dress");
            gs.agregarCancion(dmvi, 9, "Clean");
        }
    }
}
