package DesDaugtMa.core.manager;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GoogleDriveUploader {

    private final JavaPlugin plugin;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);

    public GoogleDriveUploader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void uploadFile(java.io.File uploadFile) throws Exception {
        // 1. Konfiguration laden
        String credFilePath = plugin.getConfig().getString("backup.google-drive.credentials-file", "credentials.json");
        String parentFolderId = plugin.getConfig().getString("backup.google-drive.folder-id");
        String appName = plugin.getConfig().getString("backup.google-drive.application-name", "MinecraftCore");

        // 2. HTTP Transport & Credentials aufbauen
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Lade Credentials aus Datei
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credFilePath))
                .createScoped(SCOPES);
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        // 3. Drive Service erstellen
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(appName)
                .build();

        // 4. Datei-Metadaten vorbereiten
        File fileMetadata = new File();
        fileMetadata.setName(uploadFile.getName());
        // Setze den Eltern-Ordner (wohin hochgeladen wird)
        if (parentFolderId != null && !parentFolderId.isEmpty()) {
            fileMetadata.setParents(Collections.singletonList(parentFolderId));
        }

        // 5. Datei-Inhalt vorbereiten
        FileContent mediaContent = new FileContent("application/zip", uploadFile);

        // 6. Upload durchführen
        plugin.getLogger().info("Starte Upload von: " + uploadFile.getName());
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        plugin.getLogger().info("Datei ID: " + file.getId());
    }
}