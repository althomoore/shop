package shopcart.servlets.servlet;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import lombok.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;


public class MustacheRenderer {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(MustacheRenderer.class);

    private static final String TEMPLATE_ROOT = "webApp/templates";

    private MustacheFactory mustacheFactory;

    public MustacheRenderer() {
        this(TEMPLATE_ROOT);
    }

    public MustacheRenderer(String templateRoot) {
        mustacheFactory = new DefaultMustacheFactory(templateRoot);
    }

    public String render(@NonNull String templateName, @NonNull Object model) {
        Mustache mustache = mustacheFactory.compile(templateName);
        try (StringWriter stringWriter = new StringWriter()) {
            mustache.execute(stringWriter, model).close();
            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

