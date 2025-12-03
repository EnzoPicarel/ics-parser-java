package eirb.pg203.model;

public class OutputHtml extends Output {

    @Override
    public String header() {
        return """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Mon Calendrier Exporté</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    h1 { text-align: center; color: #333; }
                    table { border-collapse: collapse; width: 100%; margin-top: 20px; box-shadow: 0 0 20px rgba(0,0,0,0.1); }
                    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                    th { background-color: #007BFF; color: white; }
                    
                    /* Styles spécifiques pour différencier Event et Todo */
                    tr.event { background-color: #e8f5e9; } /* Vert très clair */
                    tr.todo { background-color: #fff3e0; }  /* Orange très clair */
                    
                    .badge { padding: 5px 10px; border-radius: 4px; font-weight: bold; font-size: 0.8em; }
                    .badge-event { background-color: #4caf50; color: white; }
                    .badge-todo { background-color: #ff9800; color: white; }
                </style>
            </head>
            <body>
                <h1>📅 Mon Calendrier</h1>
                <table>
                    <thead>
                        <tr>
                            <th>Type</th>
                            <th>Sujet (Summary)</th>
                            <th>Date / Échéance</th>
                            <th>Lieu</th>
                            <th>Détails / Priorité</th>
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
                <p style="text-align: center; color: #777; margin-top: 20px;">Généré automatiquement par eirb.pg203</p>
            </body>
            </html>
            """;
    }

    @Override
    public String displayEvent(Event E) {
        String start = (E.start_date != null) ? E.start_date.toString() : "";
        String end = (E.end_date != null) ? E.end_date.toString() : "";
        String desc = (E.description != null) ? E.description.replace("\\n", "<br>") : ""; // saut de ligne
        String loc = (E.location != null) ? E.location : "-";

        return String.format("""
            <tr class="event">
                <td><span class="badge badge-event">ÉVÉNEMENT</span></td>
                <td><b>%s</b></td>
                <td>Début: %s<br>Fin: %s</td>
                <td>%s</td>
                <td>%s</td>
            </tr>
            """, 
            E.summary, start, end, loc, desc);
    }

    @Override
    public String displayTodo(Todo T) {
        // Gestion des champs spécifiques aux Todos
        String due = (T.due_date != null) ? T.due_date.toString() : "Pas de date";
        String prio = (T.priority != null) ? T.priority : "0";
        String prog = (T.progress != null) ? T.progress + "%" : "0%";
        String status = (T.status != null) ? T.status : "En cours";

        return String.format("""
            <tr class="todo">
                <td><span class="badge badge-todo">TÂCHE</span></td>
                <td><b>%s</b></td>
                <td>Échéance: %s</td>
                <td>-</td>
                <td>
                    Priorité: <b>%s</b><br>
                    Progression: %s<br>
                    Statut: %s
                </td>
            </tr>
            """, 
            T.summary, due, prio, prog, status);
    }
}