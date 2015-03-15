package fr.isen.shazamphoto.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.features2d.KeyPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class Monument implements Serializable {
    private long id;
    private HashMap<String, Characteristic> characteristics; // Value of the langue is the key
    private String photoPath;
    private int year;
    private int nbVisitors;
    private int nbLike;
    private Localization localization;
    private Address address;
    private KeyPoint[] keyPoints;
    private Mat descriptors;

    public static final String NAME_SERIALIZABLE = "fr.isen.shazamphoto.database.monument_serializable";

    public Monument() {
        this(0, "", "", "", 0, 0, 0, null, new Address());
    }

    public Monument(KeyPoint[] keyPoints, Mat descriptors){
        this();
        this.descriptors = descriptors;
        this.keyPoints = keyPoints;
    }

    public Monument(long id, Localization localization){
        this();
        this.id = id;
        this.localization = localization;
    }
    public Monument(long id, String name, String photoPath, String description, int year, int nbVisitors, int nbLike, Localization localization) {
        this(id, name, photoPath, description, year, nbVisitors, nbLike, localization, new Address());
    }

    public Monument(long id, String name, String photoPath, String description, int year, int nbVisitors, int nbLike, Localization localization, Address address) {
        this(id, name, photoPath, description, year, nbVisitors, nbLike, localization, address, null, null);
    }

    public Monument(long id, String name, String photoPath, String description, int year, int nbVisitors, int nbLike, Localization localization, Address address, KeyPoint[] keyPoints, Mat descriptors) {
        this.id = id;
        this.characteristics = new HashMap<>();
        Characteristic characteristic = new Characteristic(name, description, new Language(LanguageAvailable.DEFAULT_NAME, LanguageAvailable.DEFAULT_VALUE));
        this.characteristics.put(characteristic.getLanguage().getValue(), characteristic);
        this.photoPath = photoPath;
        this.year = year;
        this.nbVisitors = nbVisitors;
        this.nbLike = nbLike;
        this.localization = localization;
        this.address = address;
        this.keyPoints = keyPoints;
        this.descriptors = descriptors;
    }

    public Monument(JSONObject jsonMonument) {
        try {
            id = Long.valueOf(jsonMonument.getString("id")).longValue();
            year = Integer.valueOf(jsonMonument.getString("year")).intValue();
            photoPath = jsonMonument.getString("photopath");
            nbVisitors = Integer.valueOf(jsonMonument.getInt("nbvisitors")).intValue();
            nbLike = Integer.valueOf(jsonMonument.getInt("nblikes")).intValue();
            localization = new Localization(jsonMonument.getJSONObject("localization"));

            //Get all the characteristic monument
            this.characteristics = new HashMap<>();
            JSONArray characteristicsJSON = jsonMonument.getJSONArray("characteristics");
            int nbCharacteristic = characteristicsJSON.length();
            for (int i = 0; i < nbCharacteristic; i++) {
                JSONObject characteristicJSON = characteristicsJSON.getJSONObject(i);
                Characteristic characteristic = new Characteristic(characteristicJSON);
                this.characteristics.put(characteristic.getLanguage().getValue(), characteristic);
            }

        } catch (Exception e) {
        }

    }

    public JSONObject toJSON() {
        JSONObject jsonObj = new JSONObject();
        try {
            // Here we convert Java Object to JSON
            jsonObj.put("photopath", getPhotoPath());
            jsonObj.put("year", Integer.valueOf(getYear()).toString());
            jsonObj.put("nbvisitors", Integer.valueOf(getNbVisitors()).toString());
            jsonObj.put("nblikes", Integer.valueOf(getNbLike()).toString());

            JSONArray jsonArray = new JSONArray();
            Iterator<HashMap.Entry<String, Characteristic>> it = characteristics.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry<String, Characteristic> entry = it.next();
                jsonArray.put(entry.getValue().toJson());
            }
            jsonObj.put("characteristics", jsonArray);

            jsonObj.put("localization", localization.toJson());
            jsonObj.put("address", address.toJson());

            // Parse the JSON keypoints and the dexcriptors
            jsonObj.put(KeyPoints.KEY, KeyPoints.toJson(keyPoints));
            jsonObj.put(Descriptors.KEY, Descriptors.toJson(descriptors));

        } catch (JSONException ex) {
        }

        return jsonObj;
    }

    public HashMap<String, Characteristic> getCharacteristics() {
        return characteristics;
    }

    public Characteristic getCharacteristicByLanguage(String languageValue) {
        return this.characteristics.get(languageValue);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        if (characteristics == null || (characteristics != null &&
                characteristics.get(LanguageAvailable.DEFAULT_VALUE) == null)) {
            return "Null";
        } else {
            return characteristics.get(LanguageAvailable.DEFAULT_VALUE).getName();
        }
    }

    public void setName(String name) {
        this.characteristics.get(LanguageAvailable.DEFAULT_VALUE).setName(name);
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDescription() {
        return characteristics.get(LanguageAvailable.DEFAULT_VALUE).getDescription();
    }

    public void setDescription(String description) {
        this.characteristics.get(LanguageAvailable.DEFAULT_VALUE).setDescription(description);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNbVisitors() {
        return nbVisitors;
    }

    public void setNbVisitors(int nbVisitors) {
        this.nbVisitors = nbVisitors;
    }

    public int getNbLike() {
        return nbLike;
    }

    public void setNbLike(int nbLike) {
        this.nbLike = nbLike;
    }

    public Localization getLocalization() {
        return localization;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }

    public void setCharacteristics(HashMap<String, Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public KeyPoint[] getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(KeyPoint[] keyPoints) {
        this.keyPoints = keyPoints;
    }

    public Mat getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(Mat descriptors) {
        this.descriptors = descriptors;
    }

    public String toString(){
        return getName() +" "+Long.valueOf(getId()).toString();
    }
}

