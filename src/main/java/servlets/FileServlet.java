package servlets;

import accounts.AccountService;
import accounts.UserProfile;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/files")
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfile userProfile = AccountService.getInstance().getUserBySessionId(req.getSession().getId());
        if (userProfile == null) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }

        String path = req.getParameter("path");
        if (path == null || !path.startsWith(getHomeDirectory(userProfile.getLogin()))) {
            path = getHomeDirectory(userProfile.getLogin());

        }

        File file = new File(path);
        if(file.isDirectory()) {
            if(file.getParent() == null) {
                req.setAttribute("parentPath", "/files?path=/");
            } else {
                req.setAttribute("parentPath", "/files?path="
                        + file.getParent());
            }
            req.setAttribute("listFiles", getListFiles(file));
            req.setAttribute("listDirectories", getListDirectories(file));
        } else if(file.isFile()) {
            try (OutputStream out = resp.getOutputStream()) {
                resp.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
                resp.addHeader("Content-Length", Long.toString(file.length()));
                resp.setContentType("application/octet-stream");
                out.write(Files.readAllBytes(file.toPath()));
            }
        }
        req.setAttribute("PageGenerationTime",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));

        req.getRequestDispatcher("directory.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {

    }

    public static List<String[]> getListFiles(File file) throws IOException {
        File[] arrayFiles = file.listFiles();
        List<String[]> listFiles = new ArrayList<>();
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        for (File child : arrayFiles) {
            if (child.isFile()) {
                String[] infoFile = {
                        "/files?path=" + child.getAbsolutePath(),
                        child.getName(),
                        Long.toString(child.length()),
                        df.format(new Date(child.lastModified()))
                };
                listFiles.add(infoFile);
            }
        }
        return listFiles;
    }

    public static List<String[]> getListDirectories(File file) throws IOException {
        File[] arrayDirectories = file.listFiles();
        List<String[]> listDirectories = new ArrayList<>();
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        for (File child : arrayDirectories) {
            if (child.isDirectory()) {
                String[] infoFile = {
                        "/files?path=" + child.getAbsolutePath(),
                        child.getName(),
                        df.format(new Date(child.lastModified()))
                };
                listDirectories.add(infoFile);
            }
        }
        return listDirectories;
    }

    public static String getHomeDirectory(String login) throws IOException {
        String path = Paths.get("").toAbsolutePath().toString() + "\\home\\" + login;
        File file = new File(path);
        if (!file.exists()) {
            if(file.mkdir()) {
                return path;
            }
        }
        return path;
    }
}
