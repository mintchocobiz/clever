package biz.mintchoco.clever.wrapper;

import biz.mintchoco.clever.config.XssConfig;
import biz.mintchoco.clever.util.BeanUtils;
import biz.mintchoco.clever.util.XssUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Slf4j
public class MultipartItem implements Part {

    private FileItem fileItem;

    private HttpServletRequest request;

    public MultipartItem(FileItem fileItem, HttpServletRequest request) {
        this.fileItem = fileItem;
        this.request = request;
    }

    public String getValue() {
        try {
            if (fileItem.isFormField()) {
                if (isXssTarget()) {
                    return XssUtils.stripXss(fileItem.getString("UTF-8"));
                }
            }

        } catch (UnsupportedEncodingException ex) {
            log.error("MultipartItem error!", ex);
        }
        return null;
    }

    private InputStream applyXss() throws IOException {
        if (isXssTarget()) {
            String buf = XssUtils.stripXss(new String(fileItem.get(), "UTF-8"));
            try (InputStream is = new ByteArrayInputStream(buf.getBytes(StandardCharsets.UTF_8))) {
                return is;
            }
        } else {
            try (InputStream is = new ByteArrayInputStream(fileItem.get())) {
                return is;
            }
        }
    }

    private boolean isXssTarget() {
        XssConfig xssConfig = BeanUtils.getXssConfig();
        return !xssConfig.isExcludePath(request);
    }

    private boolean isTextContent() {
        switch(getContentType()) {
            case MediaType.TEXT_PLAIN_VALUE:
            case MediaType.APPLICATION_JSON_VALUE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (isTextContent() || fileItem.isFormField()) {
            return applyXss();
        }
        try (InputStream is = new ByteArrayInputStream(fileItem.get())) {
            return is;

        } catch(IOException ex) {
            log.error("MultipartItem error!", ex);
            return null;
        }
    }

    @Override
    public String getContentType() {
        return fileItem.getContentType();
    }

    @Override
    public String getName() {
        return fileItem.getFieldName();
    }

    @Override
    public String getSubmittedFileName() {
        return fileItem.getName();
    }

    @Override
    public long getSize() {
        return fileItem.getSize();
    }

    @Override
    public void write(String fileName) throws IOException {
    }

    @Override
    public void delete() throws IOException {
    }

    @Override
    public String getHeader(String name) {
        return fileItem.getHeaders().getHeader(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        List<String> list = new ArrayList<>();
        fileItem.getHeaders().getHeaders(name).forEachRemaining(list::add);
        return list;
    }

    @Override
    public Collection<String> getHeaderNames() {
        List<String> list = new ArrayList<>();
        fileItem.getHeaders().getHeaderNames().forEachRemaining(list::add);
        return list;
    }
}
