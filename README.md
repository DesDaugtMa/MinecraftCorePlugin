# 🛠️ Minecraft Core Plugin - Projektdokumentation

Dieses Repository enthält ein modular aufgebautes Core-System für Minecraft-Server auf Basis der Spigot-API. Der Fokus liegt auf einer sauberen Architektur sowie der Bereitstellung von Grundfunktionen in den Bereichen Spawn-Management, Informationsdarstellung und Server-Sicherheit.

## ✨ Implementierte Funktionalitäten

* **📍 Globales Spawn-System**: Zentrale Verwaltung eines globalen Spawnpunkts. Die Implementierung umfasst die automatische Teleportation bei Erstbetreten des Servers sowie beim Respawn, sofern kein individueller Spawn (Bett/Anker) vorhanden ist.
* **📊 Echtzeit-Tablist**: Dynamische Anzeige von Server-Metriken im Header und Footer der Spielerliste. Beinhaltet die Berechnung der **TPS** (Ticks Per Second), des Spieler-Pings, der aktuellen RAM-Auslastung sowie der kumulierten Spielzeit.
* **🔒 Stop-Befehl Override**: Sicherheitssystem zur Überwachung des `/stop` Befehls. Verhindert unkontrolliertes Herunterfahren durch Interzeption des Befehls und führt stattdessen eine definierte Shutdown-Routine inklusive Deaktivierung des Autostarts aus.
* **⚙️ Zentrale Konfiguration**: Verwaltung aller Nachrichten-Strings, Präfixe und Systemparameter über eine strukturierte `config.yml` mit Unterstützung für Farbcodes.

---

## 💻 Befehlsreferenz & Berechtigungen

| Befehl | Funktionalität | Berechtigung |
| :--- | :--- | :--- |
| `/setspawn` | Speichert die aktuelle Position als globalen Spawn | `core.setspawn` |
| `/core reload` | Aktualisiert die Plugin-Konfiguration zur Laufzeit | `simpletablist.admin` |
| `/stop` | Führt die gesicherte Shutdown-Routine aus | `minecraft.command.stop` |

---

## 📂 Architektur & Paketstruktur

Das Projekt ist nach dem Prinzip der Aufgabentrennung (Separation of Concerns) in folgende Pakete unterteilt:

* **`manager`**: Beinhaltet die Geschäftslogik für Datenhaltung (Spawn) und Nachrichtenverarbeitung.
* **`commands`**: Verantwortlich für die Validierung und Ausführung von Ingame-Befehlen.
* **`listeners`**: Überwachung von Server-Events wie Join, Respawn und Command-Preprocessing.
* **`tasks`**: Handhabt wiederkehrende Prozesse wie das Tablist-Update und die TPS-Überwachung.
* **`util`**: Statische Hilfsmethoden für Farbverläufe und Zeitumrechnungen.

---

## 🛠️ Technische Spezifikationen

* **Laufzeitumgebung**: Erfordert mindestens **Java 21**.
* **Abhängigkeiten**: Basiert auf der Spigot-API Version **1.21.1-R0.1-SNAPSHOT**.
* **Build-System**: Maven zur Automatisierung von Kompilierung und Shading der Ressourcen.

---

## 📝 Konfigurationsschema (`config.yml`)

Die Datei `config.yml` dient als zentrale Anlaufstelle für Anpassungen:

```yaml
# Core-System Konfiguration
prefix: "&eCore &8» &7"

spawn:
  world: world
  x: 0.0
  y: 64.0
  z: 0.0
  yaw: 0.0
  pitch: 0.0

messages:
  no-permission:
    enabled: true
    text: "%prefix%&cKeine Berechtigung."
  not-a-player:
    enabled: true
    text: "%prefix%&cNur für Spieler verfügbar."
  spawn-set:
    enabled: true
    text: "%prefix%&aSpawnpunkt erfolgreich gesetzt!"
  stop-blocked:
    enabled: true
    text: "%prefix%&cServer fährt herunter (Autostart deaktiviert)..."
  reload-success:
    enabled: true
    text: "%prefix%&aKonfiguration neu geladen."
