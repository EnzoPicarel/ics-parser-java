package eirb.pg203.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OutputHtml extends Output {

  // Format avec Heure : 04/11/2025 14:30
  private static final DateTimeFormatter HTML_DATETIME =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
          .withLocale(Locale.FRANCE)
          .withZone(ZoneId.of("Europe/Paris"));

  // Format Date seule : 04/11/2025
  private static final DateTimeFormatter HTML_DATE_ONLY =
      DateTimeFormatter.ofPattern("dd/MM/yyyy")
          .withLocale(Locale.FRANCE)
          .withZone(ZoneId.of("Europe/Paris"));

  @Override
  public String header() {
    return """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Mon Calendrier Complet</title>
                <style>
                    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 20px; color: #333; }
                    h1 { text-align: center; color: #444; }

                    table { border-collapse: collapse; width: 100%; margin-top: 20px; font-size: 0.9em; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    th, td { border: 1px solid #ddd; padding: 12px; vertical-align: top; }
                    th { background-color: #0056b3; color: white; text-align: left; }

                    /* Couleurs des lignes */
                    tr.event { background-color: #f0f9ff; } /* Bleu très pâle */
                    tr.todo { background-color: #fff8e1; }  /* Jaune/Orange très pâle */

                    /* Badges */
                    .badge { display: inline-block; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 0.85em; color: white; }
                    .badge-event { background-color: #0288d1; }
                    .badge-todo { background-color: #ffa000; }

                    /* Liste des détails */
                    ul.details { list-style-type: none; padding: 0; margin: 0; }
                    ul.details li { margin-bottom: 4px; }
                    .label { font-weight: bold; color: #555; }

                    /* Métadonnées techniques (UID, timestamps creation...) */
                    .meta-info { font-size: 0.85em; color: #777; margin-top: 8px; border-top: 1px dashed #ccc; padding-top: 4px; }
                    .meta-info div { margin-bottom: 2px; }
                </style>
            </head>
            <body>
                <h1>📅 Export Calendrier Détaillé</h1>
                <table>
                    <thead>
                        <tr>
                            <th style="width: 10%;">Type</th>
                            <th style="width: 20%;">Quoi / Où</th>
                            <th style="width: 20%;">Quand</th>
                            <th style="width: 50%;">Détails Complets</th>
                        </tr>
                    </thead>
                    <tbody>
            """;
  }

  @Override
  public String footer() {
    return """
                    </tbody>
                </table>
            </body>
            </html>
            """;
  }

  @Override
  public String displayEvent(Event E) {
    // Préparation des dates
    String start = (E.start_date != null) ? fmt(E.start_date) : "N/A";
    String end = (E.end_date != null) ? fmt(E.end_date) : "N/A";
    String location = (E.location != null && !E.location.isEmpty()) ? E.location : "Non spécifié";

    // Construction du bloc de détails
    StringBuilder details = new StringBuilder();
    details.append("<ul class='details'>");

    // Description
    if (E.description != null && !E.description.isEmpty()) {
      details
          .append("<li><span class='label'>Description :</span>")
          .append(E.description.replace("\\n", "<br>"))
          .append("</li>");
    }

    // Attendance
    if (E.attendance != null) {
      details
          .append("<li><span class='label'>Participation :</span> ")
          .append(E.attendance)
          .append("</li>");
    }

    details.append("</ul>");

    // Métadonnées techniques (UID, Creation)
    String meta = buildMetaBlock(E.uid, E.creation_date, null, null, null);

    return String.format(
        """
            <tr class="event">
                <td><span class="badge badge-event">ÉVÉNEMENT</span></td>
                <td>
                    <b>%s</b><br>
                    <small>Localisation : %s</small>
                </td>
                <td>
                    <b>Début :</b> %s<br>
                    <b>Fin :</b> %s
                </td>
                <td>
                    %s
                    %s
                </td>
            </tr>
            """,
        E.summary, location, start, end, details.toString(), meta);
  }

  @Override
  public String displayTodo(Todo T) {
    // Préparation des dates
    String start = (T.date_start != null) ? fmt(T.date_start) : "-";
    String due = (T.due_date != null) ? fmtDate(T.due_date) : "Aucune";
    String completed = (T.completed_date != null) ? fmt(T.completed_date) : "-";
    String location = (T.location != null && !T.location.isEmpty()) ? T.location : "-";

    // Construction du bloc de détails principaux
    StringBuilder details = new StringBuilder();
    details.append("<ul class='details'>");

    details
        .append("<li><span class='label'>Statut :</span> ")
        .append(T.status != null ? T.status : "N/A")
        .append("</li>");
    details
        .append("<li><span class='label'>Priorité :</span> ")
        .append(T.priority != null ? T.priority : "0")
        .append("</li>");
    details
        .append("<li><span class='label'>Progression :</span> ")
        .append(T.progress != null ? T.progress + "%" : "0%")
        .append("</li>");

    if (T.organizer != null) {
      // Nettoyage sommaire de l'organizer pour l'affichage
      String orgDisplay = T.organizer.replace("mailto:", "").replace(";", " ");
      details
          .append("<li><span class='label'>Organisateur :</span> ")
          .append(orgDisplay)
          .append("</li>");
    }

    if (T.attendance != null) {
      details
          .append("<li><span class='label'>Type (Class) :</span> ")
          .append(T.attendance)
          .append("</li>");
    }

    details.append("</ul>");

    // Métadonnées techniques
    String meta =
        buildMetaBlock(T.uid, T.creation_date, T.modification_date, T.sequence, completed);

    return String.format(
        """
            <tr class="todo">
                <td><span class="badge badge-todo">TÂCHE</span></td>
                <td>
                    <b>%s</b><br>
                    <small>Localisation : %s</small>
                </td>
                <td>
                    <b>Début :</b> %s<br>
                    <b>Échéance :</b> %s
                </td>
                <td>
                    %s
                    %s
                </td>
            </tr>
            """,
        T.summary, location, start, due, details.toString(), meta);
  }

  // --- Méthodes Utilitaires ---

  /** Construit le petit bloc gris en bas de cellule avec les infos techniques */
  private String buildMetaBlock(
      String uid, Instant created, Instant modified, String seq, String completedDate) {
    StringBuilder sb = new StringBuilder();
    sb.append("<div class='meta-info'>");

    if (uid != null) sb.append("<div>UID: ").append(uid).append("</div>");
    if (created != null) sb.append("<div>Créé le: ").append(fmt(created)).append("</div>");
    if (modified != null) sb.append("<div>Modifié le: ").append(fmt(modified)).append("</div>");
    if (completedDate != null && !completedDate.equals("-"))
      sb.append("<div>Terminé le: ").append(completedDate).append("</div>");
    if (seq != null) sb.append("<div>Séquence: ").append(seq).append("</div>");

    sb.append("</div>");
    return sb.toString();
  }

  public static String fmt(Instant i) {
    if (i == null) return null;
    return HTML_DATETIME.format(i);
  }

  public static String fmtDate(Instant i) {
    if (i == null) return null;
    return HTML_DATE_ONLY.format(i);
  }
}
