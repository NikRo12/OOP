package ru.nsu.romanenko.report;

import ru.nsu.romanenko.model.TaskResult;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HtmlReporter {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final ReportViewModel model;

    public HtmlReporter(ReportViewModel model) {
        this.model = model;
    }

    public void generate(PrintStream out) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\"><head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<title>OOP Course &mdash; Check Report</title>");
        out.println(CSS);
        out.println("</head><body>");

        out.println("<h1>OOP Course &mdash; Lab Check Report</h1>");
        out.printf("<p class='generated'>Generated: %s</p>%n",
            LocalDate.now().format(DATE_FMT));

        for (GroupView group : model.getGroups()) {
            out.printf("<h2>Group: %s</h2>%n", esc(group.name()));
            renderGroupTable(out, group);
        }

        out.println("<h2>Tasks</h2>");
        renderTaskTable(out);

        out.println("</body></html>");
    }

    private void renderGroupTable(PrintStream out, GroupView group) {
        List<TaskInfo> tasks = model.getTasks();
        List<CheckpointInfo> checkPoints = model.getCheckPoints();

        out.println("<div class='table-wrapper'><table>");

        out.println("<thead><tr>");
        out.println("<th rowspan='2'>Student</th>");
        out.println("<th rowspan='2'>GitHub</th>");
        for (TaskInfo t : tasks) {
            out.printf("<th colspan='4' class='task-header'>%s</th>%n", esc(t.id()));
        }
        out.printf("<th rowspan='2'>Total<br>score</th>%n");
        for (CheckpointInfo cp : checkPoints) {
            out.printf("<th rowspan='2'>%s<br><small>(%s)</small></th>%n",
                esc(cp.name()), fmtDate(cp.date()));
        }
        out.printf("<th rowspan='2'>Final<br>grade</th>%n");
        out.println("</tr>");

        out.println("<tr>");
        for (int i = 0; i < tasks.size(); i++) {
            out.println("<th title='Compile'>Build</th>");
            out.println("<th title='Javadoc + Style'>Docs</th>");
            out.println("<th title='Tests'>Tests</th>");
            out.println("<th title='Score'>Score</th>");
        }
        out.println("</tr></thead>");

        out.println("<tbody>");
        for (StudentView sv : group.students()) {
            renderStudentRow(out, sv, tasks);
        }
        out.println("</tbody></table></div>");
    }

    private void renderStudentRow(PrintStream out, StudentView sv,
                                  List<TaskInfo> tasks) {
        out.println("<tr>");
        out.printf("<td>%s</td>%n", esc(sv.fullName()));
        out.printf("<td><a href='https://github.com/%s' target='_blank'>%s</a></td>%n",
            esc(sv.github()), esc(sv.github()));

        for (TaskInfo t : tasks) {
            TaskResult tr = sv.taskResults().get(t.id());
            if (tr == null) {
                out.println("<td colspan='4' class='na'>&mdash;</td>");
                continue;
            }

            out.printf("<td class='%s' title='%s'>%s</td>%n",
                tr.isCompiled() ? "ok" : "err",
                tr.isCompiled() ? "OK" : escAttr(tr.getErrorMessage()),
                tr.isCompiled() ? "&#10003;" : "&#10007;");

            String docStyle = tr.isDocGenerated() && tr.isStyleOk() ? "ok"
                : (tr.isDocGenerated() || tr.isStyleOk() ? "warn" : "err");
            out.printf("<td class='%s' title='Javadoc: %s | Style: %s'>%s</td>%n",
                docStyle,
                tr.isDocGenerated() ? "OK" : "FAIL",
                tr.isStyleOk() ? "OK" : "FAIL",
                (tr.isDocGenerated() ? "&#128196;" : "&#10007;") + (tr.isStyleOk() ? "&#10003;" : "&#10007;"));

            String testLabel = tr.getTotalTests() > 0
                ? tr.getTestsPassed() + "/" + tr.getTotalTests()
                : "&mdash;";
            String testCss = tr.getTestsFailed() == 0 && tr.getTotalTests() > 0
                ? "ok" : (tr.getTestsFailed() > 0 ? "err" : "na");
            out.printf("<td class='%s' title='Passed: %d, Failed: %d, Skipped: %d'>%s</td>%n",
                testCss, tr.getTestsPassed(), tr.getTestsFailed(), tr.getTestsSkipped(), testLabel);

            String bonusNote = tr.getBonusScore() > 0 ? " (+" + tr.getBonusScore() + ")" : "";
            out.printf("<td class='score %s' title='%s'>%.1f%s</td>%n",
                tr.getStatusCssClass(), esc(tr.getStatusLabel()), tr.getTotalScore(), bonusNote);
        }

        out.printf("<td class='total'>%.1f / %.0f</td>%n", sv.totalScore(), sv.totalMax());

        for (CheckpointGradeView cp : sv.checkpointGrades()) {
            out.printf("<td class='grade'>%s<br><small>%.0f</small></td>%n",
                esc(cp.grade()), cp.score());
        }

        out.printf("<td class='grade final'>%s</td>%n", esc(sv.finalGrade()));
        out.println("</tr>");
    }

    private void renderTaskTable(PrintStream out) {
        out.println("<table class='task-legend'>");
        out.println("<thead><tr><th>ID</th><th>Title</th><th>Max score</th>"
            + "<th>Soft deadline</th><th>Hard deadline</th></tr></thead><tbody>");
        for (TaskInfo t : model.getTasks()) {
            out.printf("<tr><td>%s</td><td>%s</td><td>%d</td><td>%s</td><td>%s</td></tr>%n",
                esc(t.id()), esc(t.title()), t.maxScore(),
                fmtDate(t.softDeadline()), fmtDate(t.hardDeadline()));
        }
        out.println("</tbody></table>");
    }

    private String fmtDate(LocalDate d) {
        return d != null ? d.format(DATE_FMT) : "&mdash;";
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String escAttr(String s) {
        if (s == null) return "";
        return esc(s).replace("\"", "&quot;").replace("\n", " ").replace("\r", "");
    }

    private static final String CSS = """
        <style>
          body { font-family: 'Segoe UI', Arial, sans-serif; background: #f5f5f5;
                 color: #222; margin: 0; padding: 20px; }
          h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }
          h2 { color: #34495e; margin-top: 30px; }
          p.generated { color: #888; font-size: 0.9em; }
          .table-wrapper { overflow-x: auto; margin-bottom: 30px; }
          table { border-collapse: collapse; background: #fff;
                  box-shadow: 0 1px 4px rgba(0,0,0,.1); font-size: 0.85em; }
          th, td { border: 1px solid #ddd; padding: 6px 10px; text-align: center;
                   white-space: nowrap; }
          thead th { background: #2c3e50; color: #fff; position: sticky; top: 0; }
          thead tr:nth-child(2) th { background: #34495e; }
          tbody tr:nth-child(even) { background: #f9f9f9; }
          tbody tr:hover { background: #eaf4ff; }
          td:first-child, td:nth-child(2) { text-align: left; }
          .task-header { background: #1a6b9a !important; }
          .ok   { background: #d4edda; color: #155724; }
          .err  { background: #f8d7da; color: #721c24; }
          .warn { background: #fff3cd; color: #856404; }
          .na   { color: #999; }
          .score { font-weight: bold; color: #1a6b9a; }
          .total { font-weight: bold; background: #e8f4f8; }
          .grade { font-weight: bold; }
          .final { background: #2c3e50; color: #fff; }
          table.task-legend { margin-top: 10px; }
          table.task-legend td:first-child { font-family: monospace; }
          a { color: #3498db; text-decoration: none; }
          a:hover { text-decoration: underline; }
          small { font-size: 0.8em; opacity: 0.8; }
        </style>
        """;
}
