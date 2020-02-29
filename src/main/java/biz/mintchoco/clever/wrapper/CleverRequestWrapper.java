package biz.mintchoco.clever.wrapper;

import biz.mintchoco.clever.config.XssConfig;
import biz.mintchoco.clever.util.BeanUtils;
import biz.mintchoco.clever.util.XssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public final class CleverRequestWrapper extends HttpServletRequestWrapper implements UploadContext {

    private byte[] rawData;

    private List<MultipartItem> multipartItems = new ArrayList<>();

    public CleverRequestWrapper(HttpServletRequest request) {
        super(request);

        if (isMultipartRequest()) {
            parseMultipartParams();
        } else {
            try (InputStream is = request.getInputStream()) {
                rawData = applyXss(IOUtils.toString(is, StandardCharsets.UTF_8)).getBytes();

            } catch (IOException ex) {
                log.error("MultipartRequestWrapper error!", ex);
            }
        }
    }

    private String applyXss(String str) {
        if (checkXssTarget()) {
            return XssUtils.stripXss(str);
        } else {
            log.debug("this request is XSS EXCLUDE path");
            return str;
        }
    }

    private boolean checkXssTarget() {
        XssConfig xssConfig = BeanUtils.getXssConfig();
        return !xssConfig.isExcludePath((HttpServletRequest) getRequest());
    }

    private boolean isMultipartRequest() {
        String contentType = super.getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            return true;
        }
        return false;
    }

    private void parseMultipartParams() {
        try (InputStream is = getRequest().getInputStream()) {
            rawData = IOUtils.toByteArray(is);

            final FileItemFactory factory = new DiskFileItemFactory();
            FileUpload upload = new FileUpload(factory);
            upload.setHeaderEncoding("UTF-8");

            List<FileItem> fileItems = upload.parseRequest((RequestContext) this);
            for(FileItem fileItem : fileItems) {
                multipartItems.add(new MultipartItem(fileItem, (HttpServletRequest) getRequest()));
            }

        } catch(IOException ex) {
            log.error("MultipartRequestWrapper error!", ex);
        } catch(FileUploadException ex) {
            log.error("MultipartRequestWrapper error!", ex);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(rawData);
            ServletInputStream sis = new ServletInputStreamImpl(bis)) {
            return sis;

        } catch(IOException ex) {
            log.error("MultipartRequestWrapper error!", ex);
            return null;
        }
    }

    @Override
    public BufferedReader getReader() {
        try (InputStreamReader isr = new InputStreamReader(getInputStream(), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            return br;

        } catch(IOException ex) {
            log.error("MultipartRequestWrapper error!", ex);
            return null;
        }
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return applyXss(value);
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        for(MultipartItem item : multipartItems) {
            if(item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        List<Part> parts = new ArrayList<>();
        for (MultipartItem item : multipartItems) {
            parts.add(item);
        }
        return parts;
    }

    @Override
    public long contentLength() {
        return getRequest().getContentLength();
    }

    @Override
    public String getParameter(String name) {
        for(MultipartItem item : multipartItems) {
            if(item.getName().equals(name)) {
                return item.getValue();
            }
        }
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        List<String> list = new ArrayList<>();
        for (MultipartItem item : multipartItems) {
            if (item.getName().equals(name)) {
                list.add(item.getValue());
            }
        }
        return list.stream().toArray(String[]::new);
    }

    class ServletInputStreamImpl extends ServletInputStream {
        private InputStream is;

        public ServletInputStreamImpl(InputStream bis) {
            is = bis;
        }

        @Override
        public int read() throws IOException {
            return is.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return is.read(b);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }
    }
}

