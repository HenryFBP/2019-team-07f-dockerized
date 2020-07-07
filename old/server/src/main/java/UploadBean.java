import com.teamteem.dao.VideoDAO;
import com.teamteem.model.Person;
import com.teamteem.util.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import java.io.IOException;

@ManagedBean(name = "uploadBean")
@SessionScoped
public class UploadBean {

    @Autowired
    public VideoDAO videoDAO;

    @Autowired
    private SessionHelper sessionHelper;

    /**
     * File stream from the form.
     */
    private Part file;

    /**
     * Filename from the form.
     */
    private String fileName;

    /**
     * File extension of the file.
     */
    private static final String fileExt = "mp4";
    private static final String fileExt2 = "mp3";

    public void upload() throws IOException, Exception {

        Person person = sessionHelper.getLoggedInPerson();

        if (file.getSubmittedFileName() == null) {
            throw new NullPointerException("You cannot upload a null filename!");
        }

        if (file.getSubmittedFileName().equalsIgnoreCase("")) {
            throw new IllegalArgumentException("You cannot upload a file with an empty name!");
        }

        if (!file.getSubmittedFileName().endsWith(fileExt)) { //TODO actually show error in form instead of creating a stack trace
            throw new IllegalArgumentException(String.format("Only %s files can be uploaded!", fileExt));
        }

        if (person == null) {
            //TODO for some reason, 'user' is null. Session not being set correctly?
            throw new Exception("You are not logged in and thus cannot upload videos!");
        }

        if (file != null) {
            videoDAO.saveVideoFile(person, file, String.format("%s.%s", fileName, fileExt));
        } else {
            throw new NullPointerException(String.format("`file` is null somehow! This %s's variables are not being automatically filled in!", UploadBean.class.getSimpleName()));
        }
    }

    public void convert() throws Exception {

        Person person = sessionHelper.getLoggedInPerson();

        if (file.getSubmittedFileName() == null) {
            throw new NullPointerException("You cannot convert a null filename!");
        }

        if (file.getSubmittedFileName().equalsIgnoreCase("")) {
            throw new IllegalArgumentException("You cannot convert a file with an empty name!");
        }

        if (!file.getSubmittedFileName().endsWith(fileExt)) {
            throw new IllegalArgumentException(String.format("Only %s files can be converted!", fileExt));
        }

        if (person == null) {
            throw new Exception("You are not logged in and thus cannot convert videos!");
        }

        if (file != null) {
            videoDAO.saveAudioFile(person, file, String.format("%s.%s", fileName, fileExt2));
        } else {
            throw new NullPointerException(String.format("`file` is null somehow! This %s's variables are not being automatically filled in!", UploadBean.class.getSimpleName()));
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
