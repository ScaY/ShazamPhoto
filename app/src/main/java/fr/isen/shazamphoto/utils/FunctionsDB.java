package fr.isen.shazamphoto.utils;

import android.content.Context;

import fr.isen.shazamphoto.database.FavouriteMonumentDAO;
import fr.isen.shazamphoto.database.Monument;
import fr.isen.shazamphoto.database.MonumentDAO;
import fr.isen.shazamphoto.database.TaggedMonumentDAO;

public class FunctionsDB {

    public static void addMonumentToDB(Monument monument, Context context) {
        if (monument != null && monument.getId() == -1) {
            MonumentDAO dao = new MonumentDAO(context);
            dao.open();
            long id = dao.getMonumentId(monument);
            if (id == -1) {
                id = dao.insert(monument);
            }
            monument.setId(id);
            dao.close();
        }
    }

    public static void addMonumentToTaggedMonument(Monument monument, Context context) {
        TaggedMonumentDAO taggedMonumentDAO = new TaggedMonumentDAO(context);
        taggedMonumentDAO.open();
        if (taggedMonumentDAO.select(monument.getId()) == null) {
            taggedMonumentDAO.insert(monument);
        }
        taggedMonumentDAO.close();
    }

    public static void removeMonumentFromTaggedMonument(Monument monument, Context context) {
        TaggedMonumentDAO taggedMonumentDAO = new TaggedMonumentDAO(context);
        taggedMonumentDAO.open();
        if (taggedMonumentDAO.select(monument.getId()) != null) {
            taggedMonumentDAO.delete(monument);
        }
        taggedMonumentDAO.close();
    }

    public static void removeMonumentFromFavouriteMonument(Monument monument, Context context) {
        FavouriteMonumentDAO favouriteMonumentDAO = new FavouriteMonumentDAO(context);
        favouriteMonumentDAO.open();
        if (favouriteMonumentDAO.select(monument.getId()) != null) {
            favouriteMonumentDAO.delete(monument);
        }
        favouriteMonumentDAO.close();
    }

    public static void editMonument(Monument monument, Context context) {
        if (monument != null && monument.getId() == -1) {
            MonumentDAO dao = new MonumentDAO(context);
            dao.open();
            dao.edit(monument);
            dao.close();
        }
    }
}
