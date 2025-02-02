package me.kairo.event.manager;

import me.kairo.event.Main;
import java.sql.*;
import java.util.UUID;

public class DataBaseManager {
    private final Main plugin;
    private Connection connection;

    public DataBaseManager(Main plugin) {
        this.plugin = plugin;
        connect();
        createOrUpdateTable();
    }

    private void connect() {
        try {
            String url = "jdbc:mysql://" + plugin.getConfig().getString("mysql.host") + ":" +
                    plugin.getConfig().getInt("mysql.port") + "/" +
                    plugin.getConfig().getString("mysql.database") + "?useSSL=false";
            connection = DriverManager.getConnection(url, plugin.getConfig().getString("mysql.username"),
                    plugin.getConfig().getString("mysql.password"));
            plugin.getLogger().info("✅ Connexion MySQL réussie !");
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("❌ Échec de la connexion MySQL !");
        }
    }

    private void createOrUpdateTable() {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS rewarded_players (" +
                            "uuid VARCHAR(36) NOT NULL, " +
                            "event_name VARCHAR(50) NOT NULL, " +
                            "PRIMARY KEY(uuid, event_name))"
            );
            stmt.executeUpdate();
            plugin.getLogger().info("✅ Table 'rewarded_players' vérifiée/créée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasReceivedReward(UUID uuid, String event) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM rewarded_players WHERE uuid=? AND event_name=?"
            );
            stmt.setString(1, uuid.toString());
            stmt.setString(2, event);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addRewardedPlayer(UUID uuid, String event) {
        if (hasReceivedReward(uuid, event)) {
            plugin.getLogger().info("⏩ Le joueur " + uuid + " a déjà reçu la récompense pour l'événement '" + event + "', insertion ignorée.");
            return;
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT IGNORE INTO rewarded_players (uuid, event_name) VALUES (?, ?)"
            );
            stmt.setString(1, uuid.toString());
            stmt.setString(2, event);
            stmt.executeUpdate();
            plugin.getLogger().info("✅ Joueur " + uuid + " ajouté à l'événement '" + event + "' !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void resetEvent(String event) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM rewarded_players WHERE event_name=?");
            stmt.setString(1, event);
            stmt.executeUpdate();
            plugin.getLogger().info("🗑️ Réinitialisation de l'événement '" + event + "' !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetAllEvents() {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM rewarded_players");
            stmt.executeUpdate();
            plugin.getLogger().info("🗑️ Toutes les récompenses ont été réinitialisées !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
