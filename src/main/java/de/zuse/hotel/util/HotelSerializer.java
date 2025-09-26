package de.zuse.hotel.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.zuse.hotel.core.HotelConfiguration;
import de.zuse.hotel.gui.SettingsController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HotelSerializer
{
    ObjectMapper mapper;

    private static final String YAML_WARNING_MESSAGE = "# Do not modify this file manually";
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String SERIALIZING_LOCATION = "/src/main/resources/de/zuse/hotel/core/";
    private static final String HOTEL_FILE_NAME = "hotel.yaml";

    public HotelSerializer()
    {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
    }

    public void serializeHotel(HotelConfiguration hotelConfiguration) throws IOException
    {
        Files.createDirectories(Path.of(PROJECT_PATH + SERIALIZING_LOCATION));
        String path = PROJECT_PATH + SERIALIZING_LOCATION + HOTEL_FILE_NAME;
        File file = new File(path);


        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(YAML_WARNING_MESSAGE);

        mapper.writeValue(bufferedWriter, hotelConfiguration);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(hotelConfiguration.getRoomServices());

        bufferedWriter.close();
    }

    public HotelConfiguration deserializeHotel() throws IOException
    {
        String path = PROJECT_PATH + SERIALIZING_LOCATION + HOTEL_FILE_NAME;
        if (!canDeserialize(path))
        {
            HotelConfiguration defaultConfig = new HotelConfiguration();
            defaultConfig.setDefaultValues();
            return defaultConfig;
        }

        File file = new File(path);
        // deserialize
        HotelConfiguration hotelConfiguration = mapper.readValue(file, HotelConfiguration.class);
        ZuseCore.coreAssert(hotelConfiguration != null, "floor is null, Check Serializer!!");

        return hotelConfiguration;
    }

    public void serializeSettings() throws IOException
    {
        //TODO: serializer should print key also
        Files.createDirectories(Path.of(PROJECT_PATH + SERIALIZING_LOCATION));
        String path = PROJECT_PATH + SERIALIZING_LOCATION + "settings.yaml";
        File file = new File(path);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(YAML_WARNING_MESSAGE+"\n");

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.writeValue(bufferedWriter, SettingsController.getMode());

        bufferedWriter.close();
    }

    public void deserializeSettings() throws IOException
    {
        String path = PROJECT_PATH + SERIALIZING_LOCATION + "settings.yaml";
        if (!canDeserialize(path))
        {
            SettingsController.setMode(SettingsController.SystemMode.DARK);
            return;
        }

        File file = new File(path);
        // deserialize
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        SettingsController.SystemMode mode = objectMapper.readValue(file, SettingsController.SystemMode.class);
        ZuseCore.check(mode != null, "can not deserialize settings.yaml");
        SettingsController.setMode(mode);
    }

    public <T> boolean canDeserialize(String fileName)
    {
        File file = new File(fileName);
        if (fileName == null || file == null || !file.exists() || !file.canRead())
            return false;

        return true;
    }

}
